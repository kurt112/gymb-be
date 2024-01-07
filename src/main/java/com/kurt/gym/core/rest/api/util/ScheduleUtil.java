package com.kurt.gym.core.rest.api.util;

import com.kurt.gym.core.persistence.entity.GymClass;
import com.kurt.gym.core.persistence.entity.Schedule;
import com.kurt.gym.core.persistence.repository.ScheduleRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
public class ScheduleUtil {
    private static ScheduleRepository scheduleRepository;

    public static void initRepositories(ScheduleRepository scheduleRepository){
        ScheduleUtil.scheduleRepository = scheduleRepository;
    }

    ScheduleUtil() {

    }

    public static void save(Schedule schedule){

    }

    public static Long count(){
        return scheduleRepository.count();
    }

    public static Set<Schedule> getScheduleTargetDate(String yyyyMMddDate){
        return scheduleRepository.getScheduleTargetDate(yyyyMMddDate);
    }

    public static void deleteById(long id){
        scheduleRepository.deleteById(id);
    }

    public static GymClass getGymClassReferenceByScheduleId (Long scheduleId){
        return null;
    }
}
