package com.kurt.gym.core.serviceImpl;

import com.kurt.gym.core.persistence.repository.MembershipWithUserRepository;
import com.kurt.gym.core.rest.api.util.CustomerUtil;
import com.kurt.gym.core.rest.api.util.MembershipUtil;
import com.kurt.gym.core.persistence.entity.Customer;
import com.kurt.gym.core.persistence.entity.Membership;
import com.kurt.gym.core.persistence.entity.MembershipWithUser;
import com.kurt.gym.core.services.MembershipService;
import com.kurt.gym.helper.Charges;
import com.kurt.gym.helper.service.ApiMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
public class MembershipServiceImpl implements MembershipService {

    private final MembershipWithUserRepository membershipWithUserRepository;

    private final Logger logger = LoggerFactory.getLogger(MembershipServiceImpl.class);

    @Override
    @CachePut(cacheNames = { "memebership" }, key = "#t.id")
    @CacheEvict(value = "memberhip-table-data", allEntries = true)
    public ResponseEntity<?> save(Membership t) {
        MembershipUtil.save(t);

        return new ResponseEntity<Membership>(t, HttpStatus.OK);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = { "memebership" }, key = "#t.id"),
            @CacheEvict(value = "memberhip-table-data", allEntries = true)
    })
    public ResponseEntity<?> delete(Membership t) {

        MembershipUtil.delete(t);

        return ApiMessage.successResponse("Membership deleted");
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = { "memebership" }, key = "#id"),
            @CacheEvict(value = "memberhip-table-data", allEntries = true)
    })

    public ResponseEntity<?> deleteById(Long id) {
        MembershipUtil.deleteById(id);

        return ApiMessage.successResponse("Membership deleted");
    }

    @Override
    @Cacheable(value = "memebership", key = "#id")
    public ResponseEntity<?> findOne(Long id) {
        Membership membership = MembershipUtil.findById(id);

        if (membership == null)
            return ApiMessage.errorResponse("No Membership Found");

        return new ResponseEntity<>(membership, HttpStatus.OK);
    }

    @Override
    @Cacheable(cacheNames = "membership-reference-by-id", key="id")
    public Membership referenceById(Long id) {
        return MembershipUtil.getReferenceById(id);
    }

    @Override
    @Cacheable(value = "memberhip-table-data", key = "new org.springframework.cache.interceptor.SimpleKey(#search, #size, #page)")
    public ResponseEntity<?> data(String search, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Membership> memberships = MembershipUtil.findAllByOrderByCreatedAtDesc(search, pageable);

        return new ResponseEntity<>(memberships, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getMembershipMembers(Long membershipId, String search, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MembershipWithUser> membershipWithUserPages = MembershipUtil.getMembershipMembers(membershipId,
                pageable);
        return new ResponseEntity<>(membershipWithUserPages, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> enrollCustomer(String rfId, Long membershipId) {

        Long customerId = CustomerUtil.findCustomerIdByRfID(rfId);

        return enrollCustomerById(customerId, membershipId);
    }

    @Override
    public ResponseEntity<?> enrollCustomerById(Long customerId, Long membershipId) {

        logger.info("Enrolling Customer In Membership with ID -> " + customerId);

        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());


        if (customerId == null) {
            return ApiMessage.errorResponse("No Customer Found");
        }

        Customer customer = CustomerUtil.getReferenceById(customerId);

        Membership membership = this.referenceById(membershipId);

        if (membership == null) {
            return ApiMessage.errorResponse("Membership not found");
        }

        if (membership.getMembershipPromoExpiration() != null && currentDate.getTime().after(membership.getMembershipPromoExpiration())) {
            return ApiMessage.errorResponse("Membership promo expired");
        }

        if (customer.getMembershipDuration() != null && currentDate.getTime().after(customer.getMembershipDuration())) {
            customer.setIsMember(false);
        }

        if (customer.getIsMember()) {
            return ApiMessage.errorResponse("Customer is already a member!");
        }


        Calendar endDate = Calendar.getInstance();
        endDate.setTime(new Date());

        endDate.add(Calendar.YEAR, membership.getYear());
        endDate.add(Calendar.DATE, membership.getDay());
        endDate.add(Calendar.WEEK_OF_YEAR, membership.getYear());
        endDate.add(Calendar.MONTH, membership.getMonth());

        MembershipWithUser membershipWithUser = MembershipWithUser
                .builder()
                .price(membership.getPrice())
                .membership(membership)
                .currentEnroll(customer.getUser())
                .charge(Charges.MONTHLY)
                .currentEnroll(customer.getUser())
                .isActive(true)
                .startDate(new Date())
                .endDate(endDate.getTime())
                .build();

        membershipWithUserRepository.save(membershipWithUser);

        customer.setIsMember(true);
        customer.setMembershipDuration(endDate.getTime());

        CustomerUtil.save(customer);
        logger.info("Success Enrolling Customer Membership with ID -> " + customerId);

        return ApiMessage.successResponse("Membership enrolled successfully");
    }

    @Override
    public ResponseEntity<?> unEnrollMembershipCustomerByCustomerId(Long customerId) {

        logger.info("Un-enrolling membership Customer ID -> " + customerId);

        if (customerId == null) {
            return ApiMessage.errorResponse("No Customer Found");
        }

        Customer customer = CustomerUtil.getReferenceById(customerId);

        customer.setIsMember(false);
        customer.setMembershipDuration(null);

        Long membershipWithUserId = membershipWithUserRepository.getMembershipWithUserId(customer.getUser().getId());

        if (membershipWithUserId == null) {
            return ApiMessage.errorResponse("No Current Membership");
        }

        MembershipWithUser membershipWithUser = membershipWithUserRepository.getReferenceById(membershipWithUserId);

        if (membershipWithUser == null) {
            return ApiMessage.errorResponse("No Membership With User Found");
        }

        membershipWithUser.setIsActive(false);
        membershipWithUserRepository.save(membershipWithUser);
        CustomerUtil.save(customer);

        logger.info("Done enrolling membership Customer ID -> " + customer);
        return ApiMessage.successResponse("Customer membership pull out successfully");
    }

    @Override
    public Membership getDefaultMembership() {
        return this.referenceById(1L);
    }

    @Override
    public ResponseEntity<?> unEnrollMembershipCustomerByRfId(String rfId) {
        Long customerId = CustomerUtil.findCustomerIdByRfID(rfId);

        return unEnrollMembershipCustomerByCustomerId(customerId);
    }

}
