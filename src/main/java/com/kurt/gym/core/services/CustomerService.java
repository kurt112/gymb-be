package com.kurt.gym.core.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.auth.model.user.User;
import com.kurt.gym.core.persistence.entity.Customer;
import com.kurt.gym.helper.service.BaseService;

@Service
public interface CustomerService extends BaseService<Customer> {
  ResponseEntity<?> updateCustomerAttendanceByRfId(String rfId);

  ResponseEntity<?> getTodayCustomer(String search, int size, int page);

  ResponseEntity<?> topUpCustomer(String userTokenAssign, long userId, double amount);
  ResponseEntity<?> manualTopUpCustomer(String userTokenAssign, String firstName, String lastName, String middleName, double amount);
  ResponseEntity<?> getUserIdByCustomerRfId(String rfId);

  void deductCustomerSubscription(User user);

  ResponseEntity<?> updateCustomerAttendanceByFirstNameLastNameAndMiddleName(String firstName, String lastName, String middleName);

}
