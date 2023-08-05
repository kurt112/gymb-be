package com.kurt.gym.schedule.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.kurt.gym.schedule.model.Schedule;

import jakarta.transaction.Transactional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM schedule where id = ?1", nativeQuery = true)
    void deleteScheduleById(Long id);

    // dont medoffy this to jpql for speed performance reasons
    @Query(value = "SELECT * FROM Schedule WHERE gym_class_id = ?1", nativeQuery = true)
    List<Schedule> getGymClassSchedules(Long id);
}
