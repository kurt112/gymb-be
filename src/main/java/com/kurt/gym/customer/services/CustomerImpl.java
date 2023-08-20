package com.kurt.gym.customer.services;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.auth.model.services.user.UserRepository;
import com.kurt.gym.auth.model.user.User;
import com.kurt.gym.customer.model.Customer;
import com.kurt.gym.customer.model.CustomerAttendance;
import com.kurt.gym.gym.audit.model.AuditTrail;
import com.kurt.gym.gym.audit.service.AuditTrailService;
import com.kurt.gym.gym.membership.model.MembershipWithUser;
import com.kurt.gym.gym.membership.service.MembershipWithUserRepository;
import com.kurt.gym.gym.store.model.Store;
import com.kurt.gym.gym.store.service.StoreService;
import com.kurt.gym.helper.Action;
import com.kurt.gym.helper.Charges;
import com.kurt.gym.helper.service.ApiMessage;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class CustomerImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final StoreService storeService;
    private final AuditTrailService auditTrailService;
    private final MembershipWithUserRepository membershipWithUserRepository;

    Logger logger = LoggerFactory.getLogger(CustomerImpl.class);

    @Override
    @CachePut(cacheNames = { "customer" }, key = "#t.id")
    public ResponseEntity<Customer> save(Customer t) {
        customerRepository.saveAndFlush(t);

        return new ResponseEntity<Customer>(
                t,
                HttpStatus.OK);
    }

    @Override
    @CacheEvict(cacheNames = { "customer" }, key = "#t.id")
    public ResponseEntity<HashMap<String, String>> delete(Customer t) {

        Customer fromDb = customerRepository.findById(t.getId()).orElse(null);

        if (fromDb == null)
            return ApiMessage.errorResponse("No customer found");

        customerRepository.deleteById(t.getId());

        return ApiMessage.successResponse("Customer deleted successfully");
    }

    @Override
    @CacheEvict(cacheNames = { "customer" }, key = "#id")
    public ResponseEntity<HashMap<String, String>> deleteById(Long id) {

        Customer fromDb = customerRepository.findById(id).orElse(null);

        if (fromDb == null)
            return ApiMessage.errorResponse("No customer found");

        customerRepository.deleteById(id);

        return ApiMessage.successResponse("Customer deleted successfully");
    }

    @Override
    public ResponseEntity<Page<Customer>> data(String search, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = customerRepository.findAllByOrderByCreatedAtDesc(search, pageable);

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @Cacheable(cacheNames = "customer", key = "#id")
    public ResponseEntity<?> findOne(Long id) {
        Customer fromDb = customerRepository.findById(id).orElse(null);

        if (fromDb == null)
            return ApiMessage.errorResponse("Customer not found");

        return new ResponseEntity<Customer>(
                fromDb,
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateCustomerAttendaceByRfId(String rfId) {

        HashMap<String, String> result = new HashMap<String, String>();
        Long customerId = customerRepository.findCustomerIdByRfID(rfId);

        if (customerId == null)
            return ApiMessage.errorResponse("Customer Not Found");

        Customer customer = customerRepository.getReferenceById(customerId);
        User user = customer.getUser();

        Calendar currenDate = Calendar.getInstance();
        currenDate.setTime(new Date());

        if (user.getCardValue().doubleValue() < 0) {
            customer.setIsMember(false);
            customerRepository.save(customer);

            logger.info("Please settle balance of " + user.getCardValue().doubleValue()
                    + " and subscribe to new membership");

            throw new UnsupportedOperationException("Please settle balance of " + user.getCardValue().doubleValue()
                    + " and subscribe to new membership");
        }

        if (!customer.getIsMember()) {
            throw new UnsupportedOperationException("Customer is not a member");
        }

        deductCustomerSubscription(user);

        if (currenDate.getTime().after(customer.getMembershipDuration())) {
            customer.setIsMember(false);
            customerRepository.save(customer);
            throw new UnsupportedOperationException("Membership expired!");
        }

        boolean isCustomerIsOut = customer.getIsOut();
        String message = "Time In Successful";
        if (isCustomerIsOut) {
            customer.setTimeOut(new Date());
            message = "Time Out Successful";
        } else if (customer.getTimeIn() == null) {
            customer.setTimeIn(new Date());
        } else {
            Calendar currentDate = Calendar.getInstance();
            Calendar customerDate = Calendar.getInstance();
            customerDate.setTime(customer.getTimeIn());

            if (currentDate.get(Calendar.YEAR) == customerDate.get(Calendar.YEAR)
                    && currentDate.get(Calendar.MONTH) == customerDate.get(Calendar.MONTH)
                    && currentDate.get(Calendar.DAY_OF_MONTH) == customerDate.get(Calendar.DAY_OF_MONTH)) {
                customer.setTimeIn(new Date());
            } else {
                Set<CustomerAttendance> customerAttendance = customer.getCustomerAttendance();
                customerAttendance.add(CustomerAttendance.builder()
                        .customer(customer)
                        .timeIn(customer.getTimeIn())
                        .timeOut(customer.getTimeOut())
                        .build());
                customer.setTimeIn(new Date());
                customer.setTimeOut(null);
                customer.setCustomerAttendance(customerAttendance);
            }
        }

        customer.setIsOut(!isCustomerIsOut);

        String fullName = "";

        if (user != null) {
            fullName = user.getFirstName() + " " + user.getLastName();
        }

        customerRepository.save(customer);
        result.put("message", message);
        result.put("user", fullName);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getTodaysCustomer(String search, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = customerRepository.todaysCustomer(pageable);

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @Override
    public Customer referencedById(Long id) {
        return customerRepository.getReferenceById(id);
    }

    @Override
    public ResponseEntity<?> topUpCustomer(String userTokenAssign, long userId, double amount) {

        User user = userRepository.getReferenceById(userId);

        if (user == null) {
            return ApiMessage.errorResponse("User not found");
        }

        BigDecimal topUpAMount = BigDecimal.valueOf(amount);
        BigDecimal newCardValue = user.getCardValue().add(topUpAMount);

        Store store = user.getStore();

        user.setCardValue(newCardValue);

        BigDecimal poinsEarned = topUpAMount.divide(store.getAmountNeedToEarnOnePoint());
        BigDecimal totalPoints = poinsEarned.add(user.getPointsAmount());

        user.setPointsAmount(totalPoints);

        userRepository.save(user);
        storeService.insertSale(store, topUpAMount, new Date());

        // TODO: change the .user to the current logind user

        String assignUser = user.getLastName().toUpperCase() + ", " + user.getFirstName();
        String appliedUser = user.getLastName().toUpperCase() + ", " + user.getFirstName();

        AuditTrail auditTrail = AuditTrail
                .builder()
                .message(assignUser + " Top up customer " + appliedUser + " value of " + topUpAMount)
                .customer(user)
                .user(user)
                .action(Action.TOP_UP)
                .build();

        auditTrailService.save(auditTrail);

        return ApiMessage.successResponse("Top up success");
    }

    @Override
    public ResponseEntity<?> getUserIdByCustomerRfId(String rfId) {

        Long customerId = customerRepository.findCustomerIdByRfID(rfId);

        if (customerId == null)
            return ApiMessage.errorResponse("Customer not found");

        Customer customer = customerRepository.getReferenceById(customerId);

        return ApiMessage.successResponse("" + customer.getUser().getId());
    }

    @Override
    public void deductCustomerSubscription(User user) {

        Long userId = user.getId();

        Long membershipWithUserId = membershipWithUserRepository.getMembershipWithUserId(userId);

        if (membershipWithUserId == null) {
            logger.error("Membership with user not found with the id of user  " + userId);
            return;
        }

        MembershipWithUser membershipWithUser = membershipWithUserRepository.getReferenceById(membershipWithUserId);

        Charges charges = membershipWithUser.getCharge();

        if (charges == Charges.FREE_TRIAL) {
            logger.error("No Charges with free trial");
            return;
        }

        BigDecimal membershipPrice = new BigDecimal(membershipWithUser.getPrice());

        // if the last charge and next charge is equal null meaning the user is not yet
        // dedecuted to the membership

        BigDecimal userCurrentCardValue = user.getCardValue();
        BigDecimal newBalance = userCurrentCardValue.subtract(membershipPrice);

        Calendar nextChargeCalendar = Calendar.getInstance();

        Date currentDate = new Date();
        logger.info("Current Date -> " + currentDate);

        if (user.getLastCharge() == null && user.getNextCharge() == null) {
            user.setCardValue(newBalance);
            nextChargeCalendar.setTime(currentDate);
        } else {

            nextChargeCalendar.setTime(user.getNextCharge());

            if (nextChargeCalendar.getTime().after(currentDate)) {
                logger.info("No Charge will be deducted");
                return;
            } else if (nextChargeCalendar.getTime().before(currentDate)) {
                logger.info("Charging Balance");
                user.setCardValue(newBalance);

                // Getting the user last charge so that when we compute the charges will be
                // calculated to the last charge
                nextChargeCalendar.setTime(user.getLastCharge());
            }
        }

        switch (charges) {
            case ANNUALLY:
                nextChargeCalendar.add(Calendar.YEAR, 1);
                break;
            case WEEKLY:
                nextChargeCalendar.add(Calendar.DAY_OF_MONTH, 7);
                break;
            case QUARTERLY:
                nextChargeCalendar.add(Calendar.MONTH, 3);
                break;
            case SEMI_ANNUAL:
                nextChargeCalendar.add(Calendar.MONTH, 6);
                break;
            case MONTHLY:
                nextChargeCalendar.add(Calendar.MONTH, 1);
                break;
            case EVERYDAY:
                nextChargeCalendar.add(Calendar.DAY_OF_MONTH, 1);
                break;
            default:
                logger.error("No Charges Found for " + MembershipWithUser.class + " -> " + membershipWithUser.getId());
                break;
        }

        user.setLastCharge(new Date());
        user.setNextCharge(nextChargeCalendar.getTime());
        logger.info("Next Charge Date -> " + nextChargeCalendar.getTime());
    }

}
