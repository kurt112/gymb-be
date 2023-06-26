package com.kurt.gym.gym.membership.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kurt.gym.gym.membership.model.Membership;
import com.kurt.gym.gym.membership.model.MembershipWithUser;

import jakarta.transaction.Transactional;

@Transactional
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Page<Membership> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("select new com.kurt.gym.gym.membership.model.MembershipWithUser(e.id, e.charge, e.startDate, e.endDate, e.lastCharge, e.isActive, e.price, e.currentEnroll) from MembershipWithUser e where e.membership.id = ?1")
    Page<MembershipWithUser> getmembershipMembers(Long membershipId, Pageable pageable);    
}