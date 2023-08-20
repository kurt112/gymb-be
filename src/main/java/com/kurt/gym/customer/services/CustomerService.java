package com.kurt.gym.customer.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.auth.model.user.User;
import com.kurt.gym.customer.model.Customer;
import com.kurt.gym.helper.service.BaseService;

@Service
public interface CustomerService extends BaseService<Customer> {
  ResponseEntity<?> updateCustomerAttendaceByRfId(String rfId);

  ResponseEntity<?> getTodaysCustomer(String search, int size, int page);

  ResponseEntity<?> topUpCustomer(String userTokenAssign, long userId, double amount);

  ResponseEntity<?> getUserIdByCustomerRfId(String rfId);

  void deductCustomerSubscription(User user);

}
