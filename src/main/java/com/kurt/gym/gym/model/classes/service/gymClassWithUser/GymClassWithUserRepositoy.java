package com.kurt.gym.gym.model.classes.service.gymClassWithUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kurt.gym.gym.model.classes.GymClassWithUser;
import jakarta.transaction.Transactional;

@Transactional
public interface GymClassWithUserRepositoy extends JpaRepository<GymClassWithUser, Long> {
    @Query("select new com.kurt.gym.gym.model.classes.GymClassWithUser(e.currentEnroll, e.session, e.dateStart) from GymClassWithUser e where e.gymClass.id = ?1")
    Page<GymClassWithUser> getGymClassMembers(Long gymClassId, Pageable pageable);
}
