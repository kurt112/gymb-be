package com.kurt.gym.gym.model.membership.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kurt.gym.gym.model.membership.MembershipWithUser;

import jakarta.transaction.Transactional;

@Transactional
public interface MembershipWithUserRepository extends JpaRepository<MembershipWithUser, Long>{
    
}
