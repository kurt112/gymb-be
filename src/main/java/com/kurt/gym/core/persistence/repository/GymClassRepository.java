package com.kurt.gym.core.persistence.repository;

import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kurt.gym.core.persistence.entity.GymClass;

import jakarta.transaction.Transactional;

@Transactional
public interface GymClassRepository extends JpaRepository<GymClass, Long> {
    @Query("select e.id from GymClass e where e.id = ?1")
    Long isExist(Long id);

    @Query("select new com.kurt.gym.core.persistence.entity.GymClass(e.id, e.name, e.gymClassType, e.dateStart, e.dateEnd, e.instructorName) " +
    "from GymClass e where (e.gymClassType.name like %?1% or e.name like %?1% or e.instructorName like %?1%)")
    Page<GymClass> getGymClassWithoutSchedules(String search,Pageable pageable);

    @Query("select e from GymClass e where e.isActive = true")
    List<GymClass> getGymClassesSchedule( );
}
