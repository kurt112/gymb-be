package com.kurt.gym.core.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.core.persistence.entity.Employee;
import com.kurt.gym.helper.service.BaseService;

@Service
public interface EmployeeService extends BaseService<Employee> {
    ResponseEntity<?> getEmployeeCoachAutoComplete(String search);

    ResponseEntity<?> data(String search, Integer role, int size, int page);
}
