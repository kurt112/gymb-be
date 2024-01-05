package com.kurt.gym.gym.audit.service;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kurt.gym.gym.audit.model.AuditTrail;

import jakarta.transaction.Transactional;

@Transactional
public interface AuditTrailRepoisitory extends JpaRepository<AuditTrail, Long> {

    @Query("SELECT new com.kurt.gym.gym.audit.model.AuditTrail(e.id, e.action, e.message, e.createdAt) from AuditTrail e order by e.createdAt desc")
    Page<AuditTrail> findAuditTrailOrderByCreatedAtDesc(String search, Pageable pageable);

    @Query("SELECT new com.kurt.gym.gym.audit.model.AuditTrail(e.id, e.action, e.message, e.createdAt) from AuditTrail e where (e.createdAt between ?1 and ?2) or e.createdAt = ?1 order by e.createdAt desc")
    Page<AuditTrail> findAuditTrailFilterByStartDateAndEndDateOrderByCreatedAtDesc(Date startDate,
            Date endDate, Pageable pageable);
}
