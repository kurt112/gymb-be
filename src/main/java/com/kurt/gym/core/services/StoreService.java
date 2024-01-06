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

    ResponseEntity<HashMap<String,Object>> dashboard(Long id);


    ResponseEntity<?> getDateSale(Long id, Date date, int length);

    ResponseEntity<?> getTodaySchedules();
}
