package com.kurt.gym.gym.audit.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.gym.audit.model.AuditTrail;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditTrailServiceImpl implements AuditTrailService{

    private final AuditTrailRepoisitory auditTrailRepoisitory;

    @Override
    public ResponseEntity<?> data(String search, int size, int page) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'data'");
    }

    @Override
    public ResponseEntity<?> save(AuditTrail t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public ResponseEntity<?> delete(AuditTrail t) {
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

    @Override
    public AuditTrail referencedById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'referencedById'");
    }
    
}
