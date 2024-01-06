package com.kurt.gym.core.rest.api.util;

import com.kurt.gym.core.persistence.entity.GymClass;
import com.kurt.gym.core.persistence.repository.GymClassRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GymClassUtil {
    private static GymClassRepository gymClassRepository;

    public static void initRepositories(GymClassRepository gymClassRepository){
        GymClassUtil.gymClassRepository = gymClassRepository;
    }

    public static GymClass save(GymClass gymClass){
        gymClassRepository.save(gymClass);
        return gymClass;
    }

    public static GymClass getReferenceById(Long id){
        return gymClassRepository.getReferenceById(id);
    }

    public static GymClass getGymClassWithoutSchedules(Long id){
        return gymClassRepository.getGymClassByIdWithoutSchedules(id);
    }

}
