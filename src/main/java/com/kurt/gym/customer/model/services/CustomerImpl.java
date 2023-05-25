package com.kurt.gym.customer.model.services;

import java.util.HashMap;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.customer.model.Customer;
import com.kurt.gym.helper.service.ApiMessage;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class CustomerImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
   @CachePut(cacheNames = {"customer"}, key = "#t.id")
    public ResponseEntity<HashMap<String, String>> save(Customer t) {
        customerRepository.saveAndFlush(t);
        return ApiMessage.successResponse("Customer saved successfully");
    }

    @Override
    @CacheEvict(cacheNames = {"customer"}, key = "#t.id")
    public ResponseEntity<HashMap<String, String>> delete(Customer t) {
        if(isExist(t.getId()) == null) return ApiMessage.errorResponse("No customer found");

        customerRepository.deleteById(t.getId());

        return ApiMessage.successResponse("Customer deleted successfully");
    }

    @Override
    @CacheEvict(cacheNames = {"customer"}, key = "#id")
    public ResponseEntity<HashMap<String, String>> deleteById(Long id) {

        if(isExist(id) == null) return ApiMessage.errorResponse("No customer found");

        customerRepository.deleteById(id);

        return ApiMessage.successResponse("Customer deleted successfully");
    }

    @Override
    public Long isExist(Long id) {
        return customerRepository.isCUstomerExist(id);
    }

    @Override
    public ResponseEntity<Page<Customer>> data(String search, int size, int page) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Customer> customers = customerRepository.findAll(pageable);
        
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @Override
    @Cacheable(cacheNames = "customer", key = "#id")
    public ResponseEntity<Customer> findOne(Long id) {      
        System.out.println("i am hereee");
        Customer fromDb = customerRepository.getReferenceById(id);
        Customer customer = Customer.builder()
        .id(fromDb.getId())
        .user(fromDb.getUser())
        .createdAt(fromDb.getCreatedAt())
        .updatedAt(fromDb.getUpdatedAt())
        .build();
        return new ResponseEntity<Customer>(customer, HttpStatus.OK);
    }

    
}
