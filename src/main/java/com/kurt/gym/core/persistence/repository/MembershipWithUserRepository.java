package com.kurt.gym.core.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kurt.gym.core.persistence.entity.MembershipWithUser;

import jakarta.transaction.Transactional;

@Transactional
public interface MembershipWithUserRepository extends JpaRepository<MembershipWithUser, Long>{
    
    @Query("select e.id from MembershipWithUser e where e.currentEnroll.id = ?1 and e.isActive = 1")
    Long getMembershipWithUserId(long userId);
    
}
