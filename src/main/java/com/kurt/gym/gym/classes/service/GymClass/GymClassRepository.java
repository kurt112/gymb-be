package com.kurt.gym.gym.classes.service.GymClass;

import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kurt.gym.gym.classes.model.GymClass;

import jakarta.transaction.Transactional;

@Transactional
public interface GymClassRepository extends JpaRepository<GymClass, Long> {
    @Query("select e.id from GymClass e where e.id = ?1")
    Long isExist(Long id);

    @Query("select new com.kurt.gym.gym.classes.model.GymClass(e.id, e.name, e.type, e.dateStart, e.dateEnd) from GymClass e")
    Page<GymClass> getGymClassWithoutSchedules(Pageable pageable);

    @Query("select e from GymClass e where e.isActive = true")
    List<GymClass> getGymClassesSchedule( );
}