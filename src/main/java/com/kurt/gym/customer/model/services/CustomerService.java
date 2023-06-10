package com.kurt.gym.customer.model.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.customer.model.Customer;
import com.kurt.gym.helper.service.BaseService;

@Service
public interface CustomerService extends BaseService<Customer> {
    ResponseEntity<?> updateCustomerAttendaceByRfId(String rfId);
      ResponseEntity<?> getTodaysCustomer(String search, int size, int page);
}
