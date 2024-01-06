package com.kurt.gym.core.serviceImpl;

import com.kurt.gym.core.persistence.entity.Store;
import com.kurt.gym.core.persistence.entity.StoreSale;
import com.kurt.gym.core.persistence.repository.*;
import com.kurt.gym.core.rest.api.util.CustomerUtil;
import com.kurt.gym.core.rest.api.util.EmployeeUtil;
import com.kurt.gym.core.rest.api.util.StoreUtil;
import com.kurt.gym.core.services.StoreService;
import com.kurt.gym.helper.service.ApiMessage;
import com.kurt.gym.schedule.service.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService {

    private final StoreSaleRepository storeSaleRepository;
    private final GymClassRepository gymClassRepository;
    private final ScheduleRepository scheduleRepository;
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
    @Cacheable(value="dashboard", key="#id")
    public ResponseEntity<HashMap<String, Object>> dashboard(Long id) {
        HashMap<String, Object> result = new HashMap<>();

        Long clientCount = CustomerUtil.countCustomer();
        Long classesCount = gymClassRepository.count();
        Long coachesCount = EmployeeUtil.countCoach();
        Long scheduleCount = scheduleRepository.count();

        result.put("clientCount", clientCount);
        result.put("classesCount", classesCount);
        result.put("coachesCount", coachesCount);
        result.put("scheduleCount", scheduleCount);

        return new ResponseEntity<>(
                result,
                HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> getDateSale(Long id, Date date) {

        Double storeSale = storeSaleRepository.findSalesInStoreWithDate(id, date);

        if(storeSale == null) {
            String message = "No sales found in within date " + date;
            logger.warn(message);
            storeSale = 0D;
        }

        return new ResponseEntity<>(storeSale, HttpStatus.OK);
    }

}
