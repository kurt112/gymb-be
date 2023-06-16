package com.kurt.gym.gym.model.membership.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kurt.gym.gym.model.membership.Membership;
import com.kurt.gym.gym.model.membership.MembershipWithUser;

import jakarta.transaction.Transactional;

@Transactional
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Page<Membership> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("select new com.kurt.gym.gym.model.membership.MembershipWithUser(e.id, e.charge, e.startDate, e.endDate, e.lastCharge, e.isActive, e.price, e.currentEnroll) from MembershipWithUser e where e.membership.id = ?1")
    Page<MembershipWithUser> getmembershipMembers(Long membershipId, Pageable pageable);
}