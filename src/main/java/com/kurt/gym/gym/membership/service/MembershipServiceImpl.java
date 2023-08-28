package com.kurt.gym.gym.membership.service;

import java.util.Calendar;
import java.util.Date;

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

import com.kurt.gym.customer.model.Customer;
import com.kurt.gym.customer.services.CustomerRepository;
import com.kurt.gym.gym.membership.model.Membership;
import com.kurt.gym.gym.membership.model.MembershipWithUser;
import com.kurt.gym.helper.Charges;
import com.kurt.gym.helper.service.ApiMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository membershipRepository;
    private final MembershipWithUserRepository membershipWithUserRepository;
    private final CustomerRepository customerRepository;

    @Override
    @CachePut(cacheNames = { "memebership" }, key = "#t.id")
    @CacheEvict(value = "memberhip-table-data", allEntries = true)
    public ResponseEntity<?> save(Membership t) {
        membershipRepository.save(t);

        return new ResponseEntity<Membership>(t, HttpStatus.OK);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = { "memebership" }, key = "#t.id"),
            @CacheEvict(value = "memberhip-table-data", allEntries = true)
    })
    public ResponseEntity<?> delete(Membership t) {

        membershipRepository.delete(t);

        return ApiMessage.successResponse("Membership deleted");
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = { "memebership" }, key = "#id"),
            @CacheEvict(value = "memberhip-table-data", allEntries = true)
    })

    public ResponseEntity<?> deleteById(Long id) {
        membershipRepository.deleteById(id);

        return ApiMessage.successResponse("Membership deleted");
    }

    @Override
    @Cacheable(value = "memebership", key = "#id")
    public ResponseEntity<?> findOne(Long id) {
        System.out.println("i am here");
        Membership membership = membershipRepository.findById(id).orElse(null);

        if (membership == null)
            return ApiMessage.errorResponse("No Membership Found");

        return new ResponseEntity<Membership>(membership, HttpStatus.OK);
    }

    @Override
    @Cacheable(value = "memberhip-table-data", key = "new org.springframework.cache.interceptor.SimpleKey(#search, #size, #page)")
    public ResponseEntity<?> data(String search, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Membership> memberships = membershipRepository.findAllByOrderByCreatedAtDesc(search, pageable);

        return new ResponseEntity<>(memberships, HttpStatus.OK);
    }

    @Override
    public Membership referencedById(Long id) {
        return membershipRepository.getReferenceById(id);
    }

    @Override
    public ResponseEntity<?> getMembershipMembers(long membershipId, String search, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MembershipWithUser> membershipWithUserPages = membershipRepository.getmembershipMembers(membershipId,
                pageable);
        return new ResponseEntity<>(membershipWithUserPages, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> enrollCustomer(String rfId, long membershipId) {

        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());

        Long customerId = customerRepository.findCustomerIdByRfID(rfId);

        if (customerId == null) {
            return ApiMessage.errorResponse("No Customer Found");
        }

        Customer customer = customerRepository.getReferenceById(customerId);

        Membership membership = membershipRepository.getReferenceById(membershipId);

        if (membership == null) {
            return ApiMessage.errorResponse("Membership not found");
        }

        if (currentDate.getTime().after(membership.getMembershipPromoExpiration())) {
            return ApiMessage.errorResponse("Membership promo expired");
        }

        if (customer.getMembershipDuration() != null && currentDate.getTime().after(customer.getMembershipDuration())) {
            customer.setIsMember(false);
        }

        if (customer.getIsMember()) {
            return ApiMessage.errorResponse("Customer is already a member!");
        }

        System.out.println("i am here");

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

        customerRepository.save(customer);

        return ApiMessage.successResponse("Membership enrolled successfully");
    }

    @Override
    public ResponseEntity<?> unEnrollMembershipCustomer(String rfId) {
        Long customerId = customerRepository.findCustomerIdByRfID(rfId);

        if (customerId == null) {
            return ApiMessage.errorResponse("No Customer Found");
        }

        Customer customer = customerRepository.getReferenceById(customerId);

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
        customerRepository.save(customer);

        return ApiMessage.successResponse("Customer membership pull out successfully");
    }

}
