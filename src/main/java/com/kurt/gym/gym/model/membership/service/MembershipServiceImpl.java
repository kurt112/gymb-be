package com.kurt.gym.gym.model.membership.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.gym.model.membership.Membership;
import com.kurt.gym.helper.service.ApiMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository membershipRepository;

    @Override
    @CachePut(cacheNames = {"memebership"}, key= "#t.id")
    public ResponseEntity<?> save(Membership t) {
        membershipRepository.save(t);

        return new ResponseEntity<Membership>(t, HttpStatus.OK);
    }

    @Override
    @CacheEvict(cacheNames = {"memebership"}, key= "#t.id")
    public ResponseEntity<?> delete(Membership t) {

        membershipRepository.delete(t);

        return ApiMessage.successResponse("Membership deleted");
    }

    @Override
    @CacheEvict(cacheNames = {"memebership"}, key= "#id")
    public ResponseEntity<?> deleteById(Long id) {
        membershipRepository.deleteById(id);

        return ApiMessage.successResponse("Membership deleted");
    }

    @Override
    public Long isExist(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isExist'");
    }

    @Override
    @Cacheable(value = "memebership", key = "#id")
    public ResponseEntity<?> findOne(Long id) {
       Membership membership = membershipRepository.findById(id).orElse(null);

       if (membership == null) return ApiMessage.errorResponse("No Membership Found");

       return new ResponseEntity<Membership>(membership, HttpStatus.OK);
    }
    
}
