package com.kurt.gym.helper.service;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface BaseService<T>{

    ResponseEntity<?> data(String search, int size, int page);
   
    ResponseEntity<?> save(T t);
   
    ResponseEntity<?> delete(T t);

    ResponseEntity<?> deleteById(Long id);

    ResponseEntity<?> findOne(Long id);

    T referenceById(Long id);
}
