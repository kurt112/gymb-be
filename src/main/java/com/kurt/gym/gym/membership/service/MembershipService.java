package com.kurt.gym.gym.membership.service;

import org.springframework.http.ResponseEntity;

import com.kurt.gym.gym.membership.model.Membership;
import com.kurt.gym.helper.service.BaseService;

public interface MembershipService extends BaseService<Membership> {

    ResponseEntity<?> getMembershipMembers(long membershipId, String search, int size, int page);

    ResponseEntity<?> enrollCustomer(String rfId, long membershipId);

    ResponseEntity<?> unEnrollMembershipCustomer(String rfId);

}
