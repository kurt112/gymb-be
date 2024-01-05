package com.kurt.gym.gym.membership.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.core.persistence.entity.MembershipWithUser;
import com.kurt.gym.helper.service.ApiMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembershipWithUserServiceImpl implements MembershipWithUserService {

    private final MembershipWithUserRepository membershipWithUserRepository;

    @Override
    public ResponseEntity<?> save(MembershipWithUser t) {
        membershipWithUserRepository.save(t);

        return new ResponseEntity<MembershipWithUser>(t, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> delete(MembershipWithUser t) {
        membershipWithUserRepository.deleteById(t.getId());

        return ApiMessage.errorResponse("MembershipWithUser not found");
    }

    @Override
    public ResponseEntity<?> deleteById(Long id) {
        membershipWithUserRepository.deleteById(id);

        return ApiMessage.errorResponse("MembershipWithUser not found");
    }

    @Override
    public ResponseEntity<?> findOne(Long id) {
        MembershipWithUser membershipWithUser = membershipWithUserRepository.findById(id).orElse(null);

        if(membershipWithUser == null) return ApiMessage.errorResponse("Membership with user not found");
   
        return new ResponseEntity<>(membershipWithUser, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> data(String search, int size, int page) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'data'");
    }

    public MembershipWithUser referencedById(Long id) {
        return membershipWithUserRepository.getReferenceById(id);
    }

}
