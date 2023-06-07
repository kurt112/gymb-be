package com.kurt.gym.helper.service;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface BaseService<T>{
   
    ResponseEntity<?> save(T t);
   
    ResponseEntity<?> delete(T t);

    ResponseEntity<?> deleteById(Long id);

    ResponseEntity<?> findOne(Long id);
}
