package com.kurt.gym.gym.membership.service;

import org.springframework.http.ResponseEntity;

import com.kurt.gym.core.persistence.entity.Membership;
import com.kurt.gym.helper.service.BaseService;

public interface MembershipService extends BaseService<Membership> {

    ResponseEntity<?> getMembershipMembers(Long membershipId, String search, int size, int page);

    ResponseEntity<?> enrollCustomer(String rfId, Long membershipId);

    ResponseEntity<?> enrollCustomerById(Long customerId, Long membershipId);

    ResponseEntity<?> unEnrollMembershipCustomerByRfId(String rfId);

    ResponseEntity<?> unEnrollMembershipCustomerByCustomerId(Long customerId);


    Membership getDefaultMembership();

}
