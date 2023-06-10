package com.kurt.gym.gym.model.membership.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kurt.gym.gym.model.membership.Membership;

import jakarta.transaction.Transactional;

@Transactional
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Page<Membership> findAllByOrderByCreatedAtDesc(Pageable pageable);
}