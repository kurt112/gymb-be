package com.kurt.gym.gym.audit.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kurt.gym.gym.audit.model.AuditTrail;

import jakarta.transaction.Transactional;

@Transactional
public interface AuditTrailRepoisitory extends JpaRepository<AuditTrail, Long>{
    
}
