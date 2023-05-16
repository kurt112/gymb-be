package com.kurt.gym.helper.service;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface BaseService<T>{
   
    ResponseEntity<HashMap<String, String>> save(T t);
   
    ResponseEntity<HashMap<String, String>> delete(T t);

    ResponseEntity<HashMap<String, String>> deleteById(Long id);

    Long isExist(Long id);
}
