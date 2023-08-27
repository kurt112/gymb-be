package com.kurt.gym.gym.store.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.gym.store.model.Store;
import com.kurt.gym.helper.service.BaseService;

@Service
public interface StoreService extends BaseService<Store>{
    void insertSale(Store store, BigDecimal value, Date date);

    ResponseEntity<?> dashboard(Long id);
}
