package com.kurt.gym.core.serviceImpl;

import com.kurt.gym.core.persistence.repository.GymClassWithUserRepository;
import com.kurt.gym.core.services.GymClassWithUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.core.persistence.entity.GymClassWithUser;
import com.kurt.gym.core.persistence.repository.GymClassRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GymClassWithUserServiceImpl implements GymClassWithUserService {

    private final GymClassWithUserRepository gymClassWithUserRepository;
    private final GymClassRepository gymClassRepository;

    @Override
    public ResponseEntity<?> data(String search, int size, int page) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'data'");
    }

    @Override
    public ResponseEntity<?> save(GymClassWithUser t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public ResponseEntity<?> delete(GymClassWithUser t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public ResponseEntity<?> deleteById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public ResponseEntity<?> findOne(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    public GymClassWithUser referencedById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'referencedById'");
    }
    
}
