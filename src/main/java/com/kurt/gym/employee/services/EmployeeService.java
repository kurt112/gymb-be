package com.kurt.gym.employee.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.employee.model.Employee;
import com.kurt.gym.helper.service.BaseService;

@Service
public interface EmployeeService extends BaseService<Employee> {
    ResponseEntity<?> getEmployeeCoachAutoComplete(String search);

    ResponseEntity<?> data(String search, String role, int size, int page);
}
