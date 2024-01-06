package com.kurt.gym.core.serviceImpl;

import com.kurt.gym.auth.model.services.user.UserRepository;
import com.kurt.gym.auth.model.user.User;
import com.kurt.gym.auth.model.user.UserRole;
import com.kurt.gym.core.persistence.entity.*;
import com.kurt.gym.core.persistence.repository.MembershipWithUserRepository;
import com.kurt.gym.core.rest.api.util.CustomerUtil;
import com.kurt.gym.core.rest.api.util.StoreUtil;
import com.kurt.gym.core.services.AuditTrailService;
import com.kurt.gym.core.services.CustomerService;
import com.kurt.gym.core.services.MembershipService;
import com.kurt.gym.core.services.StoreService;
import com.kurt.gym.helper.Action;
import com.kurt.gym.helper.Charges;
import com.kurt.gym.helper.service.ApiMessage;
import com.kurt.gym.infrastructure.jwt.Jwt;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

@RequiredArgsConstructor
@Transactional
@Service
public class CustomerServiceImpl implements CustomerService {
    private final UserRepository userRepository;
    private final AuditTrailService auditTrailService;
    private final MembershipWithUserRepository membershipWithUserRepository;
    private final MembershipService membershipService;
    private final Jwt jwt;

    private final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Override
    public ResponseEntity<Customer> save(Customer t) {
        t.getUser().activate(StoreUtil.getDefaultStore());
        t.getUser().setRole(UserRole.CUSTOMER);
        CustomerUtil.save(t);

        return new ResponseEntity<>(
                t,
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HashMap<String, String>> delete(Customer t) {

        Customer fromDb = CustomerUtil.findById(t.getId());

        if (fromDb == null)
            return ApiMessage.errorResponse("No customer found");

        CustomerUtil.delete(t);

        return ApiMessage.successResponse("Customer deleted successfully");
    }

    @Override
    public ResponseEntity<HashMap<String, String>> deleteById(Long id) {

        Customer fromDb = CustomerUtil.findById(id);

        if (fromDb == null)
            return ApiMessage.errorResponse("No customer found");

        CustomerUtil.deleteById(id);

        return ApiMessage.successResponse("Customer deleted successfully");
    }

    @Override
    public ResponseEntity<Page<Customer>> data(String search, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = CustomerUtil.findAllByOrderByCreatedAtDesc(search, pageable);

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    public ResponseEntity<?> findOne(Long id) {
        Customer fromDb = CustomerUtil.findById(id);

        if (fromDb == null)
            return ApiMessage.errorResponse("Customer not found");

        return new ResponseEntity<>(
                fromDb,
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateCustomerAttendanceByRfId(String rfId) {

        HashMap<String, String> result = new HashMap<>();
        Long customerId = CustomerUtil.findCustomerIdByRfID(rfId);

        if (customerId == null)
            return ApiMessage.errorResponse("Customer Not Found");

        Customer customer = CustomerUtil.getReferenceById(customerId);
        User user = customer.getUser();

        updateCustomerAttendance(customer,user, result);

        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getTodayCustomer(String search, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = CustomerUtil.todayCustomer(pageable);

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> topUpCustomer(String userTokenAssign, long userId, double amount) {

        User user = userRepository.getReferenceById(userId);
        User assignedUser = jwt.getUserInToken(userTokenAssign);
        if (user == null) {
            return ApiMessage.errorResponse("User not found");
        }

        topUp(user,assignedUser,amount);

        return ApiMessage.successResponse("Top up success");
    }

    @Override
    public ResponseEntity<?> manualTopUpCustomer(String userTokenAssign, String firstName, String lastName, String middleName, double amount) {

        Customer customer =  CustomerUtil.findCustomerByFirstNameLastNameAndMiddleName(firstName,lastName,middleName);

        if(customer.getUser() == null)  return ApiMessage.errorResponse("User not found");

        User assignedUser = jwt.getUserInToken(userTokenAssign);
        topUp(customer.getUser(),assignedUser,amount);

        return ApiMessage.successResponse("Top up success");
    }

    @Override
    public ResponseEntity<?> getUserIdByCustomerRfId(String rfId) {

        Long customerId = CustomerUtil.findCustomerIdByRfID(rfId);

        if (customerId == null)
            return ApiMessage.errorResponse("Customer not found");

        Customer customer = CustomerUtil.getReferenceById(customerId);

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

        BigDecimal membershipPrice = BigDecimal.valueOf(membershipWithUser.getPrice());

        // if the last charge and next charge is equal null meaning the user is not yet
        // deducted to the membership

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

    @Override
    public ResponseEntity<?> updateCustomerAttendanceByFirstNameLastNameAndMiddleName(String firstName, String lastName, String middleName) {
        Customer customer =  CustomerUtil.findCustomerByFirstNameLastNameAndMiddleName(firstName,lastName,middleName);
        HashMap<String, String> result = new HashMap<>();

        updateCustomerAttendance(customer,customer.getUser(), result);

        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }

    private void updateCustomerAttendance(Customer customer, User user, HashMap<String,String> result){
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());

        // if the customer is freeze we can't time in or out
        if(customer.getStatus() == CustomerStatus.FREEZE){
            logger.warn("Customer is on freeze please un-freeze to continue");
            throw new UnsupportedOperationException("Customer is on freeze please un-freeze to continue");
        }

        // if the customer has pending balance
        if(customer.getStatus() == CustomerStatus.IN_ACTIVE){
            logger.warn("Customer is in-active top - up to become active");
            throw new UnsupportedOperationException("Customer is active top - up to become active");
        }

        // if the customer in our out but has pending balance
        // then we will set the status to in active
        if (user.getCardValue().doubleValue() < 0) {
            customer.setStatus(CustomerStatus.IN_ACTIVE);
            CustomerUtil.save(customer);

            logger.info("Please settle balance of " + user.getCardValue().doubleValue()
                    + " or top-up your account to pay the balance");

            throw new UnsupportedOperationException("Please settle balance of " + user.getCardValue().doubleValue()
                    + " and subscribe to new membership");
        }

        if (customer.getMembershipDuration() == null || (customer.getStatus() == CustomerStatus.MEMBER && currentDate.getTime().after(customer.getMembershipDuration()))){
            customer.setStatus(CustomerStatus.NON_MEMBER);
            CustomerUtil.save(customer);
            membershipService.unEnrollMembershipCustomerByCustomerId(customer.getId());
            membershipService.enrollCustomerById(customer.getId(),membershipService.getDefaultMembership().getId());
        }

        boolean isCustomerIsOut = customer.getIsOut();
        String message = "Time In Successful";
        if (isCustomerIsOut) {
            customer.setTimeOut(new Date());
            message = "Time Out Successful";
        } else if (customer.getTimeIn() == null) {
            customer.setTimeIn(new Date());
        } else {
            currentDate = Calendar.getInstance();
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

        String fullName = user.getFirstName() + " " + user.getLastName();

        CustomerUtil.save(customer);
        result.put("message", message);
        result.put("user", fullName);

        deductCustomerSubscription(user);
    }

    private void topUp(User user, User assignedUser, double amount){
        BigDecimal topUpAMount = BigDecimal.valueOf(amount);
        BigDecimal newCardValue = user.getCardValue().add(topUpAMount);

        Store store = user.getStore();

        user.setCardValue(newCardValue);

        BigDecimal pointsEarned = topUpAMount.divide(store.getAmountNeedToEarnOnePoint(),2, RoundingMode.DOWN);
        BigDecimal totalPoints = pointsEarned.add(user.getPointsAmount());

        user.setPointsAmount(totalPoints);

        userRepository.save(user);
        StoreUtil.insertSale(store, topUpAMount, new Date());

        // TODO: change the .user to the current login user

        String assignUser = assignedUser.getLastName().toUpperCase() + ", " + assignedUser.getFirstName();
        String appliedUser = user.getLastName().toUpperCase() + ", " + user.getFirstName();

        AuditTrail auditTrail = AuditTrail
                .builder()
                .message(assignUser + " Top up to customer " + appliedUser + " value of " + topUpAMount)
                .customer(user)
                .user(assignedUser)
                .action(Action.TOP_UP)
                .build();

        auditTrailService.save(auditTrail);
    }

}
