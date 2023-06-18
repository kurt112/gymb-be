package com.kurt.gym.gym.model.classes.service.GymClass;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kurt.gym.gym.model.classes.GymClass;

import jakarta.transaction.Transactional;

@Transactional
public interface GymClassRepository extends JpaRepository<GymClass, Long> {
    @Query("select e.id from GymClass e where e.id = ?1")
    Long isExist(Long id);

    @Query("select new com.kurt.gym.gym.model.classes.GymClass(e.id, e.name, e.type, e.dateStart, e.dateEnd) from GymClass e")
    Page<GymClass> getGymClassWithoutSchedules(Pageable pageable);
}