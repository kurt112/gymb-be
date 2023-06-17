package com.kurt.gym.gym.model.classes.service.GymClass;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.gym.model.classes.GymClass;
import com.kurt.gym.helper.service.BaseService;

@Service
public interface GymClassService extends BaseService<GymClass> {

    ResponseEntity<?> getGymClassMembers(long gymClassId, String search, int size, int page);
}
