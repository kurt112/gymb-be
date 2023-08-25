package com.kurt.gym.gym.audit.service;

import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.gym.audit.model.AuditTrail;
import com.kurt.gym.helper.service.BaseService;

@Service
public interface AuditTrailService extends BaseService<AuditTrail>{
       ResponseEntity<?> data(String search, int size, int page, Date startDate, Date endDate);
   
}
