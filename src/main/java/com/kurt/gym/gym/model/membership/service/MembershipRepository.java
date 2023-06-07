package com.kurt.gym.gym.model.membership.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kurt.gym.gym.model.membership.Membership;

import jakarta.transaction.Transactional;

@Transactional
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    
}
