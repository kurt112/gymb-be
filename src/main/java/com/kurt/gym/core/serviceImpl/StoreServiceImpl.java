package com.kurt.gym.core.serviceImpl;

import com.kurt.gym.core.persistence.entity.GymClass;
import com.kurt.gym.core.persistence.entity.GymClassType;
import com.kurt.gym.core.persistence.entity.Schedule;
import com.kurt.gym.core.persistence.entity.Store;
import com.kurt.gym.core.persistence.repository.GymClassRepository;
import com.kurt.gym.core.rest.api.util.*;
import com.kurt.gym.core.services.StoreService;
import com.kurt.gym.helper.service.ApiMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService {

    private final GymClassRepository gymClassRepository;
    private final Logger logger = LoggerFactory.getLogger(StoreServiceImpl.class);

    @Override
    public ResponseEntity<HashMap<String, String>> save(Store t) {
        StoreUtil.save(t);

        return ApiMessage.successResponse("Store Data Saved Successfully");
    }

    @Override
    public ResponseEntity<HashMap<String, String>> delete(Store t) {
        StoreUtil.delete(t);

        return ApiMessage.successResponse("Store Data Deleted Successfully");
    }

    @Override
    public ResponseEntity<HashMap<String, String>> deleteById(Long id) {
        return ApiMessage.successResponse("Store Data Deleted Successfully");
    }

    @Override
    public ResponseEntity<?> data(String search, int size, int page) {

        List<Store> stores = StoreUtil.getAllStore();

        return new ResponseEntity<>(stores, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> findOne(Long id) {
        Store store = StoreUtil.findById(id);

        if (store == null)
            return ApiMessage.errorResponse("Customer not found");

        return new ResponseEntity<>(
                store,
                HttpStatus.OK);
    }


    // wil update every 1 hour
    @Override
    @Cacheable(value = "dashboard", key = "#id")
    public ResponseEntity<HashMap<String, Object>> dashboard(Long id) {
        HashMap<String, Object> result = new HashMap<>();

        Long clientCount = CustomerUtil.countCustomer();
        Long classesCount = gymClassRepository.count();
        Long coachesCount = EmployeeUtil.countCoach();
        Long scheduleCount = ScheduleUtil.count();

        result.put("clientCount", clientCount);
        result.put("classesCount", classesCount);
        result.put("coachesCount", coachesCount);
        result.put("scheduleCount", scheduleCount);

        return new ResponseEntity<>(
                result,
                HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> getDateSale(Long id, Date date, int length) {

        HashMap<String, Double> result = new HashMap<>();
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(date);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        while (length != 0) {

            Double storeSale = StoreUtil.findSalesInStoreBetweenDate(id, startDate.getTime());

            String requiredDate = df.format(startDate.getTime());

            result.put(requiredDate, storeSale);

            startDate.add(Calendar.DAY_OF_MONTH, -1);
            length--;
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getTodaySchedules() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateTime = now.format(formatter);

        logger.info("======================== Getting Today Schedule ==================");
        logger.info(formattedDateTime);
        Set<Schedule> todaySchedule = ScheduleUtil.getScheduleTargetDate(formattedDateTime);

        List<GymClass> gymClasses = todaySchedule.stream().map(e -> {
            GymClass gymClass = GymClassUtil.getGymClassWithoutSchedules(e.getGymClass().getId());
            gymClass.getSchedules().add(e);
            return gymClass;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(gymClasses, HttpStatus.OK);
    }

}
