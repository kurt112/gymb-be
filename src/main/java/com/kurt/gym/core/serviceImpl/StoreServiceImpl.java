package com.kurt.gym.gym.store.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.core.persistence.repository.CustomerRepository;
import com.kurt.gym.core.persistence.repository.EmployeeRepository;
import com.kurt.gym.core.persistence.repository.GymClassRepository;
import com.kurt.gym.gym.store.model.Store;
import com.kurt.gym.gym.store.model.StoreSale;
import com.kurt.gym.helper.service.ApiMessage;
import com.kurt.gym.schedule.service.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreSaleRepository storeSaleRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final GymClassRepository gymClassRepository;
    private final ScheduleRepository scheduleRepository;
    private final Logger logger = LoggerFactory.getLogger(StoreServiceImpl.class);
    @Override
    public ResponseEntity<HashMap<String, String>> save(Store t) {
        this.storeRepository.save(t);

        return ApiMessage.successResponse("Store Data Saved Successfully");
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "store-reference", key = "#t.id"),
            @CacheEvict(value = "store", key = "#t.id")
    })
    public ResponseEntity<HashMap<String, String>> delete(Store t) {
        this.storeRepository.delete(t);

        return ApiMessage.successResponse("Store Data Deleted Successfully");
    }

    @Override
    public ResponseEntity<HashMap<String, String>> deleteById(Long id) {
        return ApiMessage.successResponse("Store Data Deleted Successfully");
    }

    @Override
    public ResponseEntity<?> data(String search, int size, int page) {

        List<Store> stores = this.storeRepository.findAll();

        return new ResponseEntity<>(stores, HttpStatus.OK);
    }

    @Override
    @Cacheable(value = "store", key = "#id")
    public ResponseEntity<?> findOne(Long id) {
        Store store = this.storeRepository.findById(id).orElse(null);

        if (store == null)
            return ApiMessage.errorResponse("Customer not found");

        return new ResponseEntity<>(
                store,
                HttpStatus.OK);
    }

    @Cacheable(value = "store-reference", key = "#id")
    public Store referencedById(Long id) {
        return storeRepository.getReferenceById(id);
    }

    @Override
    public void insertSale(Store store, BigDecimal value, Date date) {
        if (store == null) {
            // default store
            store = storeRepository.getReferenceById(1L);
        }

        BigDecimal storeCurrentValue = store.getTotalSales();
        storeCurrentValue = storeCurrentValue.add(value);

        StoreSale storeSale = storeSaleRepository.findStoreSaleByCreatedAt(date);

        if (storeSale == null) {
            storeSale = StoreSale.builder()
                    .sales(value)
                    .store(store)
                    .build();
        } else {
            BigDecimal newTotalSaleWithinDay = storeSale.getSales();
            newTotalSaleWithinDay = newTotalSaleWithinDay.add(value);
            storeSale.setSales(newTotalSaleWithinDay);
        }

        store.setTotalSales(storeCurrentValue);

        storeSaleRepository.saveAndFlush(storeSale);
        storeRepository.saveAndFlush(store);
    }

    // wil update every 1 hour
    @Override
    @Cacheable(value="dashboard", key="#id")
    public ResponseEntity<HashMap<String, Object>> dashboard(Long id) {
        HashMap<String, Object> result = new HashMap<>();

        Long clientCount = customerRepository.count();
        Long classesCount = gymClassRepository.count();
        Long coachesCount = employeeRepository.countCoach();
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
    @Cacheable(value = "default_store")
    public Store getDefaultStore() {
        return storeRepository.getReferenceById(1L);
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
