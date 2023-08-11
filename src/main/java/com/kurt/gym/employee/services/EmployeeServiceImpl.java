package com.kurt.gym.employee.services;

import java.util.HashMap;
import java.util.List;

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
import com.kurt.gym.helper.model.AutoComplete;
import com.kurt.gym.helper.service.ApiMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    @CachePut(cacheNames = { "employee" }, key = "#t.id")
    public ResponseEntity<Employee> save(Employee t) {
        employeeRepository.save(t);

        return new ResponseEntity<Employee>(
                t,
                HttpStatus.OK);
    }

    @Override
    @CacheEvict(cacheNames = { "employee" }, key = "#t.id")
    public ResponseEntity<HashMap<String, String>> delete(Employee t) {
        Employee dbEmp = employeeRepository.findById(t.getId()).orElse(null);
        if (dbEmp != null)
            return ApiMessage.errorResponse("Employees not found");
        employeeRepository.delete(t);
        return ApiMessage.successResponse("Employees deleted successfully");
    }

    @Override
    @CacheEvict(cacheNames = { "employee" }, key = "#id")
    public ResponseEntity<HashMap<String, String>> deleteById(Long id) {
        Employee dbEmp = employeeRepository.findById(id).orElse(null);
        if (dbEmp != null)
            return ApiMessage.errorResponse("Employees not found");

        employeeRepository.deleteById(id);
        return ApiMessage.successResponse("Employee deleted successfully");
    }

    @Override
    public ResponseEntity<Page<Employee>> data(String search, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employees = employeeRepository.findAllByOrderByCreatedAtDesc(search, pageable);

        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @Override
    @Cacheable(cacheNames = "employee", key = "#id")
    public ResponseEntity<?> findOne(Long id) {
        Employee employeeFromDb = employeeRepository.findById(id).orElse(null);

        if (employeeFromDb == null)
            return ApiMessage.errorResponse("Employee not found");

        return new ResponseEntity<Employee>(
                Employee.buildEmployeeFromReference(employeeFromDb),
                HttpStatus.OK);
    }

    @Override
    public Employee referencedById(Long id) {
        return employeeRepository.getReferenceById(id);
    }

    @Override
    public ResponseEntity<?> getEmployeeCoachAutoComplete(String search) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<AutoComplete> employees = employeeRepository.findEmployeeCoachBySearch(search, pageable);

        System.out.println(employees.getContent());

        return new ResponseEntity<List<AutoComplete>>(employees.getContent(), HttpStatus.OK);
    }
}
