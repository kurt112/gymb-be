package com.kurt.gym.classes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kurt.gym.core.persistence.entity.GymClass;
import com.kurt.gym.core.persistence.entity.GymClassType;
import com.kurt.gym.core.persistence.entity.Schedule;
import com.kurt.gym.helper.logger.LoggerUtil;
import org.json.JSONException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:test.properties")
class GymClassTest {

    static GymClass gymClass;

    static GymClassType gymClassType;
    static List<Schedule> scheduleList;

    private static RestTemplate restTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(GymClassTest.class);
    private final static String URI = "http://localhost:8080/api/v1/gym/classes";

    private static HttpHeaders headers;

    @BeforeAll
    static void initialData() throws JSONException {

        LoggerUtil.printInfoWithDash(LOGGER, "INITIALIZING DATA");
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);


        Calendar dateStart = Calendar.getInstance();
        dateStart.set(Calendar.MONTH, Calendar.JANUARY);

        Calendar dateEnd = Calendar.getInstance();
        dateEnd.add(Calendar.YEAR, 1);

        // the start of schedule
        Calendar timeStart = Calendar.getInstance();
        timeStart.setTime(dateStart.getTime());

        // setting the time to 00:00 = 24 hour format
        timeStart.set(Calendar.HOUR, 0);
        timeStart.set(Calendar.MINUTE, 0);

        Calendar timeEnd = Calendar.getInstance();
        timeEnd.setTime(dateEnd.getTime());

        List<Schedule> schedulesList = new ArrayList<>();

        schedulesList.add(Schedule.builder().startTime(timeStart.getTime()).endTime(timeEnd.getTime()).gymClass(gymClass).build());

//        schedulesList.add(Schedule.builder().startTime(timeStart.getTime()).endTime(timeEnd.getTime()).gymClass(gymClass).build());

        gymClassType = GymClassType.builder()
                .name("Gym Class Test 102")
                .build();

        gymClass = GymClass
                .builder()
                .name("Test Gym Class 102")
                .price(20)
                .session(20)
                .allowedNonMembers(false)
                .gymClassType(gymClassType)
                .dateStart(dateStart.getTime())
                .dateEnd(dateEnd.getTime())
                .schedules(schedulesList)
                .build();


        HttpEntity<GymClass> request =
                new HttpEntity<>(gymClass, headers);

        ResponseEntity<GymClass> result = restTemplate.postForEntity(URI, request, GymClass.class);

        assertNotNull(result.getBody());

        gymClass = result.getBody();

        LoggerUtil.printInfoWithDash(LOGGER, "DONE INITIALIZING");
    }

    @Test
    void verifyIfGymClassExist() {
        ObjectMapper mapper = new ObjectMapper();

        assertNotNull(gymClass.getId());
        ResponseEntity<GymClass> result = restTemplate.getForEntity(URI + "/" + gymClass.getId(), GymClass.class);
        assertNotNull(result.getBody());
        assertEquals(gymClass.getId(), result.getBody().getId());


        ResponseEntity<List<Schedule>> getScheduleList = restTemplate.getForEntity(URI + "/" + gymClass.getId() + "/schedules", (Class<List<Schedule>>) (Object) List.class);



        assertNotNull(getScheduleList.getBody());

        scheduleList = mapper.convertValue(getScheduleList.getBody(), new TypeReference<>() {});


        assertNotNull(scheduleList);
    }

    @Test
    void createSchedule() {



    }

    @AfterAll
    static void deleteAllDateCreated() {
        LoggerUtil.printInfoWithDash(LOGGER, "DELETING DATA USE IN GYM CLASS");

        scheduleList.forEach(schedule -> assertDoesNotThrow(() -> restTemplate.delete(URI + "/" + gymClass.getId() + "/schedules/" + schedule.getId(), String.class)));

        assertDoesNotThrow(() -> restTemplate.delete(URI + "/" + gymClass.getId(), String.class));

        LoggerUtil.printInfoWithDash(LOGGER, "DONE DELETING");
    }
}