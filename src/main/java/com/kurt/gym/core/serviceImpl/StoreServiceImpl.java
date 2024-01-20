package com.kurt.gym.core.serviceImpl;

import com.kurt.gym.core.persistence.helper.data.SalesAndVat;
import com.kurt.gym.core.persistence.entity.*;
import com.kurt.gym.core.persistence.repository.GymClassRepository;
import com.kurt.gym.core.rest.api.util.*;
import com.kurt.gym.core.services.StoreService;
import com.kurt.gym.helper.service.ApiMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService {

    private final GymClassRepository gymClassRepository;
    private final Logger logger = LoggerFactory.getLogger(StoreServiceImpl.class);

    @Override
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "fid-store-by-id", key = "#t.id")
            }
    )
    public ResponseEntity<HashMap<String, String>> save(Store t) {
        StoreUtil.save(t);

        return ApiMessage.successResponse("Store Data Saved Successfully");
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "fid-store-by-id", key = "#t.id")
            }
    )
    public ResponseEntity<HashMap<String, String>> delete(Store t) {
        StoreUtil.delete(t);

        return ApiMessage.successResponse("Store Data Deleted Successfully");
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "find-store-by-id", key = "#t.id")
            }
    )
    public ResponseEntity<HashMap<String, String>> deleteById(Long id) {
        return ApiMessage.successResponse("Store Data Deleted Successfully");
    }

    @Override
    public ResponseEntity<?> data(String search, int size, int page) {

        List<Store> stores = StoreUtil.getAllStore();

        return new ResponseEntity<>(stores, HttpStatus.OK);
    }

    @Override
    @Cacheable(cacheNames = "find-store-by-id", key = "#id")
    public ResponseEntity<?> findOne(Long id) {
        Store store = StoreUtil.findById(id);

        if (store == null)
            return ApiMessage.errorResponse("Customer not found");

        return new ResponseEntity<>(
                store,
                HttpStatus.OK);
    }

    @Override
    @Cacheable(cacheNames = "store-reference-by-id", key = "id")
    public Store referenceById(Long id) {
        return StoreUtil.getReferenceById(id);
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
    @Cacheable(value = "store-sales", key = "new org.springframework.cache.interceptor.SimpleKey(#id, #date, #length)")
    public ResponseEntity<?> getDateSale(Long id, Date date, int length) {
        HashMap<String, SalesAndVat> result = new HashMap<>();
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(date);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        while (length != 0) {

            Double storeSale = StoreUtil.findSalesInStoreBetweenDate(id, startDate.getTime());
            Double storeVat = StoreUtil.findVatInStoreBetweenDate(id, startDate.getTime());

            if (storeSale == null) storeSale = 0D;
            if (storeVat == null) storeVat = 0D;

            String requiredDate = df.format(startDate.getTime());

            result.put(requiredDate, new SalesAndVat(storeSale, storeVat));

            startDate.add(Calendar.DAY_OF_MONTH, -1);
            length--;
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    @Cacheable(cacheNames = "today-schedules")
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
        }).toList();

        return new ResponseEntity<>(gymClasses, HttpStatus.OK);
    }

}
