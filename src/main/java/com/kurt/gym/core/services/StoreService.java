package com.kurt.gym.core.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.core.persistence.entity.Store;
import com.kurt.gym.helper.service.BaseService;

@Service
public interface StoreService extends BaseService<Store>{
    void insertSale(Store store, BigDecimal value, Date date);

    ResponseEntity<HashMap<String,Object>> dashboard(Long id);

    Store getDefaultStore();

    ResponseEntity<?> getDateSale(Long id, Date date);
}
