package com.kurt.gym.gym.classes.service.gymClassWithUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kurt.gym.gym.classes.model.GymClassWithUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.transaction.Transactional;

@Transactional
public interface GymClassWithUserRepositoy extends JpaRepository<GymClassWithUser, Long> {
    @Query("select new com.kurt.gym.gym.classes.model.GymClassWithUser(e.currentEnroll, e.session, e.dateStart) from GymClassWithUser e where e.gymClass.id = ?1")
    Page<GymClassWithUser> getGymClassMembers(Long gymClassId, Pageable pageable);

    @Query("select e.id from GymClassWithUser e where e.gymClass.id = ?1 and e.currentEnroll.id = ?2")
    Long getGymClassWithUser(Long gymClassId, Long userId);
    
}