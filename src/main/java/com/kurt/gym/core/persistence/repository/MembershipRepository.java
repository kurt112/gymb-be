package com.kurt.gym.core.persistence.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kurt.gym.core.persistence.entity.Membership;
import com.kurt.gym.core.persistence.entity.MembershipWithUser;

import jakarta.transaction.Transactional;

@Transactional
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    @Query("SELECT new com.kurt.gym.core.persistence.entity.Membership(e.id, e.code, e.name, e.price, e.membershipPromoExpiration, e.charge, e.createdAt) from Membership e where (e.code like %?1%)")
    Page<Membership> findAllByOrderByCreatedAtDesc(String search,Pageable pageable);

    @Query("SELECT new com.kurt.gym.core.persistence.entity.MembershipWithUser(e.id, e.charge, e.startDate, e.endDate, e.lastCharge, e.isActive, e.price, e.currentEnroll) from MembershipWithUser e where e.membership.id = ?1")
    Page<MembershipWithUser> getMembershipMembers(Long membershipId, Pageable pageable);
}