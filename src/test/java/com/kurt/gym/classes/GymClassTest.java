package com.kurt.gym.classes;

import com.kurt.gym.core.persistence.entity.GymClass;
import com.kurt.gym.core.persistence.entity.GymClassType;
import com.kurt.gym.core.persistence.entity.ScheduleData;
import com.kurt.gym.core.rest.api.util.GymClassUtil;
import com.kurt.gym.core.rest.api.util.ScheduleUtil;
import com.kurt.gym.util.schedule.ScheduleUtilTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Calendar;
import java.util.List;

@DataJpaTest()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GymClassTest {

    static GymClass gymClass;
    static String initialClassName = "Initial Class";
    private static int default_pricing = 20;
    private static int defaulltSession = 20;

    @BeforeAll
    static void initialData() {

    }

    @Test
    void createSchedule() {

        Calendar dateStart = Calendar.getInstance();
        dateStart.set(2024, Calendar.JANUARY, 1);

        Calendar dateEnd = Calendar.getInstance();
        dateStart.set(2024, Calendar.DECEMBER, 1);

        gymClass = GymClass
                .builder()
                .name("Sample Gym Class")
                .dateStart(dateStart.getTime())
                .dateEnd(dateEnd.getTime())
                .price(default_pricing)
                .session(defaulltSession)
                .build();

        try (var ms = Mockito.mockStatic(GymClassUtil.class)) {
//             Mockito.when(GymClassUtil.save(gymClass)).thenReturn(GymClass);

            System.out.println("The id");
            GymClassUtil.save(gymClass);
            System.out.println(gymClass.getId());
//            GymClassType gymClassType = GymClassType.builder()
//                    .name(initialClassName)
//                    .build();
//
//            Mockito.when(GymClassUtil.save(gymClass)).thenReturn(gymClass);
//
//            GymClassUtil.saveGymClassType(gymClassType);
//            gymClassType.getGymClasses().add(gymClass);
//            GymClassUtil.saveGymClassType(gymClassType);

        } catch (Exception e) {
            e.printStackTrace();
        }

//

//

//
//        List<ScheduleData> scheduleData = ScheduleUtilTest.generateScheduleDataList();
//
//        GymClassUtil.generateSchedule(gymClass, scheduleData);
    }

    @AfterAll
    static void deleteAllDateCreated() {

//        gymClass.getSchedules().forEach(schedule -> ScheduleUtil.deleteById(schedule.getId()));
//
//        GymClassUtil.deleteGymClassTypeById(gymClass.getGymClassType().getId());
//        GymClassUtil.deleteById(gymClass.getId());

    }
}