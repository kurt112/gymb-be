package com.kurt.gym.employee.services;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.employee.model.Employee;
import com.kurt.gym.helper.service.BaseService;

@Service
public interface EmployeeService extends BaseService<Employee>{
    
}
