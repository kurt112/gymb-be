package com.kurt.gym.util.schedule;

import com.kurt.gym.core.persistence.entity.ScheduleData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public final class ScheduleUtilTest {
    List<ScheduleData> scheduleDataList = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(ScheduleUtilTest.class);
    ;


    public static List<ScheduleData> generateScheduleDataList(){

        List<ScheduleData> scheduleData = new ArrayList<>();


        for(int i = 1; i<7; i++){
            scheduleData.add(new ScheduleData((short) i, new Time(1704038400000L), new Time(1704078000000L)));
        }

        logger.info("The Generated Data");

        scheduleData.forEach(data -> logger.info(data.toString()));


        return scheduleData;
    }

}
