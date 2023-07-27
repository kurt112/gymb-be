package com.kurt.gym.gym.classes.service.GymClass;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.gym.classes.model.GymClass;
import com.kurt.gym.helper.service.BaseService;
import com.kurt.gym.schedule.model.ScheduleData;

@Service
public interface GymClassService extends BaseService<GymClass> {

    ResponseEntity<?> getGymClassMembers(long gymClassId, String search, int size, int page);

    ResponseEntity<?> enrollCustomer(String rfId, long gymClassId);

    ResponseEntity<?> unEnrollGymClassCustomer(String rfId, long gymClassId);

    ResponseEntity<?> getGymClasses();

    ResponseEntity<?> generateGymClassSchedule(long gymClassId, List<ScheduleData> scheduleDatas);
}
