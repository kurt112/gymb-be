package com.kurt.gym.gym.model.membership.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.customer.model.Customer;
import com.kurt.gym.customer.model.services.CustomerRepository;
import com.kurt.gym.gym.model.membership.Membership;
import com.kurt.gym.gym.model.membership.MembershipWithUser;
import com.kurt.gym.helper.Charges;
import com.kurt.gym.helper.service.ApiMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository membershipRepository;
    private final MembershipWithUserRepository membershipWithUserRMembershipWithUserRepository;
    private final CustomerRepository customerRepository;

    @Override
    @CachePut(cacheNames = { "memebership" }, key = "#t.id")
    public ResponseEntity<?> save(Membership t) {
        membershipRepository.save(t);

        return new ResponseEntity<Membership>(t, HttpStatus.OK);
    }

    @Override
    @CacheEvict(cacheNames = { "memebership" }, key = "#t.id")
    public ResponseEntity<?> delete(Membership t) {

        membershipRepository.delete(t);

        return ApiMessage.successResponse("Membership deleted");
    }

    @Override
    @CacheEvict(cacheNames = { "memebership" }, key = "#id")
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

        // System.out.println(membership.getMembers());

        return new ResponseEntity<Membership>(membership, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> data(String search, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Membership> memberships = membershipRepository.findAllByOrderByCreatedAtDesc(pageable);

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
             throw new UnsupportedOperationException("No Customer Found");
        }

        Customer customer = customerRepository.getReferenceById(customerId);

        Membership membership = membershipRepository.getReferenceById(membershipId);

        if (membership == null) {
           throw new UnsupportedOperationException("Unimplemented method membership null");
        }

        if(currentDate.getTime().after(customer.getMembershipDuration())){
            customer.setIsMember(false);
        }
    
        if(customer.getIsMember()){
            throw new UnsupportedOperationException("Customer is already a member!");
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

        membershipWithUserRMembershipWithUserRepository.save(membershipWithUser);

        customer.setIsMember(true);
        customer.setMembershipDuration(endDate.getTime());

        customerRepository.save(customer);

        return ApiMessage.successResponse("Membership enrolled successfully");
    }

}
