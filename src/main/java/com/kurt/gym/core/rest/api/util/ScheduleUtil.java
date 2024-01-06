package com.kurt.gym.core.rest.api.util;

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

    public static Set<Schedule> getScheduleTargetDate(Date date){
        return scheduleRepository.getScheduleTargetDate(date);
    }
}
