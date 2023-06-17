package com.kurt.gym.gym.model.membership.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kurt.gym.gym.model.membership.MembershipWithUser;

import jakarta.transaction.Transactional;

@Transactional
public interface MembershipWithUserRepository extends JpaRepository<MembershipWithUser, Long>{
    
    @Query("select e.id from MembershipWithUser e where e.id = ?1 and e.isActive = 1")
    Long getMembershipWithUserId(long userId);
}
