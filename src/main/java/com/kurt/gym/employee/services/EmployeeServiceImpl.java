package com.kurt.gym.employee.services;

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

import com.kurt.gym.employee.model.Employee;
import com.kurt.gym.helper.service.ApiMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    @CachePut(cacheNames = { "employee" }, key = "#t.id")
    public ResponseEntity<HashMap<String, String>> save(Employee t) {
        employeeRepository.save(t);

        return ApiMessage.successResponse("Employees saved successfully");
    }

    @Override
    @CacheEvict(cacheNames = { "employee" }, key = "#t.id")
    public ResponseEntity<HashMap<String, String>> delete(Employee t) {
        if (isExist(t.getId()) == null)
            return ApiMessage.errorResponse("Employees deleted successfully");
        employeeRepository.delete(t);
        return ApiMessage.successResponse("Employees deleted successfully");
    }

    @Override
    @CacheEvict(cacheNames = { "employee" }, key = "#id")
    public ResponseEntity<HashMap<String, String>> deleteById(Long id) {
        if (isExist(id) == null)
            return ApiMessage.errorResponse("Employee deleted successfully");
        employeeRepository.deleteById(id);
        return ApiMessage.successResponse("Employee deleted successfully");
    }

    @Override
    public Long isExist(Long id) {
        return employeeRepository.isExist(id);
    }

    @Override
    public ResponseEntity<Page<Employee>> data(String search, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employees = employeeRepository.findAll(pageable);

        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @Override
    @Cacheable(cacheNames = "employee", key = "#id")
    public ResponseEntity<Employee> findOne(Long id) {
        Employee employeeFromDb = employeeRepository.getReferenceById(id);
        return new ResponseEntity<Employee>(
                Employee.buildEmployeeFromReference(employeeFromDb),
                HttpStatus.OK);
    }
}
