package com.kurt.gym.gym.audit.service;


import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.gym.audit.model.AuditTrail;
import com.kurt.gym.helper.service.ApiMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditTrailServiceImpl implements AuditTrailService {

    private final AuditTrailRepoisitory auditTrailRepoisitory;

    @Override
    public ResponseEntity<?> data(String search, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuditTrail> audiTrails = auditTrailRepoisitory.findAuditTrailOrderByCreatedAtDesc(search, pageable);

        return new ResponseEntity<>(audiTrails, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> data(String search, int size, int page, Date startDate, Date endDate) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuditTrail> audiTrails = auditTrailRepoisitory
                .findAuditTrailFilterByStartDateAndEndDateOrderByCreatedAtDesc(startDate, endDate, pageable);
        return new ResponseEntity<>(audiTrails, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> save(AuditTrail t) {
        this.auditTrailRepoisitory.save(t);
        return ApiMessage.successResponse("AuditTrail saved");
    }

    @Override
    public ResponseEntity<?> delete(AuditTrail t) {
        this.auditTrailRepoisitory.delete(t);

        return ApiMessage.successResponse("Delete AuditTrail successfully");
    }

    @Override
    public ResponseEntity<?> deleteById(Long id) {
        this.auditTrailRepoisitory.deleteById(id);

        return ApiMessage.successResponse("Delete AuditTrail successfully");
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
