package com.kurt.gym.core.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.core.persistence.entity.GymClass;
import com.kurt.gym.core.persistence.entity.GymClassType;
import com.kurt.gym.helper.service.BaseService;
import com.kurt.gym.core.persistence.entity.Schedule;
import com.kurt.gym.core.persistence.entity.ScheduleData;

@Service
public interface GymClassService extends BaseService<GymClass> {

    ResponseEntity<?> getGymClassMembers(long gymClassId, String search, int size, int page);

    ResponseEntity<?> enrollCustomer(String rfId, long gymClassId);

    ResponseEntity<?> unEnrollGymClassCustomer(String rfId, long gymClassId);

    ResponseEntity<?> getGymClasses();

    ResponseEntity<?> generateGymClassSchedule(long gymClassId, List<ScheduleData> scheduleDatas);

    ResponseEntity<?> getGymClassSchedule(long gymClassId);

    ResponseEntity<?> assignGymClassInstructor(long gymClassId, long instructorId);

    ResponseEntity<?> saveGymClassType(GymClassType gymClassType);

    ResponseEntity<?> getGymClassType(Long id);

    ResponseEntity<?> getGymClassTypes();

    ResponseEntity<?> deleteGymClassType(Long id);

    ResponseEntity<?> deleteGymClassSchedule(Long gymClassId, Long scheduleId);

    ResponseEntity<?> saveGymClassSchedule(Long gynClassId, Schedule schedule);
}
