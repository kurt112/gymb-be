package com.kurt.gym.gym.classes.service.GymClass;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.gym.classes.model.GymClass;
import com.kurt.gym.helper.service.BaseService;

@Service
public interface GymClassService extends BaseService<GymClass> {

    ResponseEntity<?> getGymClassMembers(long gymClassId, String search, int size, int page);

    ResponseEntity<?> enrollCustomer(String rfId, long gymClassId);

    ResponseEntity<?> unEnrollGymClassCustomer(String rfId, long gymClassId);

    ResponseEntity<?> getGymClasses();
}
