package com.kurt.gym.core.serviceImpl;

import com.kurt.gym.auth.model.user.UserRole;
import com.kurt.gym.core.persistence.entity.Employee;
import com.kurt.gym.core.rest.api.util.EmployeeUtil;
import com.kurt.gym.core.rest.api.util.StoreUtil;
import com.kurt.gym.core.services.EmployeeService;
import com.kurt.gym.helper.model.AutoComplete;
import com.kurt.gym.helper.service.ApiMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "employee-data", allEntries = true),
            @CacheEvict(cacheNames = "employee-coach", allEntries = true),
    })
    @CachePut(cacheNames = "employee", key = "#t.id")
    public ResponseEntity<Employee> save(Employee t) {
        t.getUser().activate(StoreUtil.getDefaultStore());
        EmployeeUtil.save(t);

        return new ResponseEntity<>(
                t,
                HttpStatus.OK);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = { "employee" }, key = "#t.id"),
            @CacheEvict(cacheNames = {"employee-data", "employee-coach"}, allEntries = true),
            @CacheEvict(cacheNames = { "employee-reference" }, key = "#t.id"),
    })
    public ResponseEntity<HashMap<String, String>> delete(Employee t) {
        Employee dbEmp = EmployeeUtil.findById(t.getId());
        if (dbEmp != null)
            return ApiMessage.errorResponse("Employees not found");
        EmployeeUtil.delete(t);
        return ApiMessage.successResponse("Employees deleted successfully");
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = { "employee" }, key = "#id"),
            @CacheEvict(cacheNames = {"employee-data","employee-coach"}, allEntries = true),
            @CacheEvict(cacheNames = { "employee-reference" }, key = "#id")
    })
    public ResponseEntity<HashMap<String, String>> deleteById(Long id) {
        Employee dbEmp = EmployeeUtil.findById(id);
        if (dbEmp != null)
            return ApiMessage.errorResponse("Employees not found");

        EmployeeUtil.deleteById(id);
        return ApiMessage.successResponse("Employee deleted successfully");
    }

    @Override
    @Cacheable(value = "employee-data", key = "new org.springframework.cache.interceptor.SimpleKey(#search, #size, #page)")
    public ResponseEntity<Page<Employee>> data(String search, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employees = EmployeeUtil.findAllByOrderByCreatedAtDesc(search, pageable);

        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @Override
    @Cacheable(cacheNames = "employee", key = "#id")
    public ResponseEntity<?> findOne(Long id) {
        Employee employeeFromDb = EmployeeUtil.findById(id);

        if (employeeFromDb == null)
            return ApiMessage.errorResponse("Employee not found");

        return new ResponseEntity<>(
                Employee.buildEmployeeFromReference(employeeFromDb),
                HttpStatus.OK);
    }

    @Override
    @Cacheable(cacheNames = {"employee-reference"}, key = "#id")
    public Employee referenceById(Long id) {
        return EmployeeUtil.getReferenceById(id);
    }

    @Override
    @Cacheable(cacheNames = "employee-coach", key = "#search")
    public ResponseEntity<?> getEmployeeCoachAutoComplete(String search) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<AutoComplete> employees = EmployeeUtil.findEmployeeCoachBySearch(search, pageable);

        return new ResponseEntity<>(employees.getContent(), HttpStatus.OK);
    }

    @Override
    @Cacheable(value = "employee-data", key = "new org.springframework.cache.interceptor.SimpleKey(#search, #size, #role, #page)")
    public ResponseEntity<?> data(String search, Integer role, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        UserRole userRole = UserRole.getRole(role);

        Page<Employee> employees = EmployeeUtil.findAllEmployeeWithRoleByOrderByCreatedAtDesc(search, userRole,
                pageable);

        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
}
