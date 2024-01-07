package com.kurt.gym.core.rest.api.util;

import com.kurt.gym.core.persistence.entity.GymClass;
import com.kurt.gym.core.persistence.entity.GymClassType;
import com.kurt.gym.core.persistence.entity.Schedule;
import com.kurt.gym.core.persistence.entity.ScheduleData;
import com.kurt.gym.core.persistence.repository.GymClassRepository;
import com.kurt.gym.core.persistence.repository.GymClassTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GymClassUtil {
    private static GymClassRepository gymClassRepository;
    private static GymClassTypeRepository gymClassTypeRepository;

    public static void initRepositories(GymClassRepository gymClassRepository, GymClassTypeRepository gymClassTypeRepository){
        GymClassUtil.gymClassRepository = gymClassRepository;
        GymClassUtil.gymClassTypeRepository = gymClassTypeRepository;
    }

    public static GymClass save(GymClass gymClass){
        gymClassRepository.save(gymClass);
        System.out.println("I am sivainggggg");
        return gymClass;
    }

    public static void deleteById(Long id){
        gymClassRepository.deleteById(id);
    }

    public static GymClass getReferenceById(Long id){
        return gymClassRepository.getReferenceById(id);
    }

    public static GymClass getGymClassWithoutSchedules(Long id){
        return gymClassRepository.getGymClassByIdWithoutSchedules(id);
    }

    public static Page<GymClass> getGymClassWithoutSchedulesPagable(String search, Pageable pageable){
        return gymClassRepository.getGymClassWithoutSchedules(search, pageable);
    }


    // --------------------------- Fro gym clas type ----------------------------------------

    public static GymClassType saveGymClassType(GymClassType gymClassType){
        gymClassTypeRepository.save(gymClassType);

        return  gymClassType;
    }

    public static void deleteGymClassTypeById(Long id){
        gymClassTypeRepository.deleteById(id);
    }

    public static GymClass findById(Long id) {
        return gymClassRepository.findById(id).orElse(null);
    }

    public static List<GymClass> getGymClassesSchedule() {
        return gymClassRepository.getGymClassesSchedule();
    }

    public static void generateSchedule(GymClass currentGymClass, List<ScheduleData> scheduleDatas){
        // Looping the schedule data so that we can assure the arrangement of schedule
        // with day

        HashMap<Short, ScheduleData> scheduleMap = new HashMap<>();

        for (ScheduleData scheduleData : scheduleDatas) {
            scheduleMap.put(scheduleData.getDay(), scheduleData);
        }

        List<Schedule> newSchedules = new ArrayList<>();

        Calendar dateStart = Calendar.getInstance();
        dateStart.setTime(currentGymClass.getDateStart());

        Calendar dateEnd = Calendar.getInstance();
        dateEnd.setTime(currentGymClass.getDateEnd());

        dateEnd.add(Calendar.DAY_OF_WEEK, 1);

        currentGymClass.getSchedules().forEach(e -> {
            ScheduleUtil.deleteById(e.getId());
        });

        while (dateStart.before(dateEnd)) {

            int currentDay = dateStart.get(Calendar.DAY_OF_WEEK);

            // in js the Sunday the inedex is zero so we minus 1
            short possibleDaySched = (short) (currentDay - 1);

            ScheduleData scheduleData = scheduleMap.get(possibleDaySched);

            if (scheduleData == null) {
                dateStart.add(Calendar.DAY_OF_WEEK, 1);
                continue;
            }

            Date startDateWithTime = scheduleData.getStartTime();
            Date endDateWithTime = scheduleData.getEndTime();

            Calendar startDateWithTimeCalendar = Calendar.getInstance();
            startDateWithTimeCalendar.setTime(startDateWithTime);
            startDateWithTimeCalendar.set(dateStart.get(Calendar.YEAR),
                    dateStart.get(Calendar.MONTH),
                    dateStart.get(Calendar.DAY_OF_MONTH));

            Calendar endDateWithTimeCalendar = Calendar.getInstance();
            endDateWithTimeCalendar.setTime(endDateWithTime);
            endDateWithTimeCalendar.set(dateStart.get(Calendar.YEAR),
                    dateStart.get(Calendar.MONTH),
                    dateStart.get(Calendar.DAY_OF_MONTH));

            Schedule schedule = Schedule
                    .builder()
                    .endTime(endDateWithTimeCalendar.getTime())
                    .startTime(startDateWithTimeCalendar.getTime())
                    .gymClass(currentGymClass)
                    .instructor(currentGymClass.getInstructor())
                    .build();

            newSchedules.add(schedule);
            ScheduleUtil.save(schedule);

            dateStart.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
}
