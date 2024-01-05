package com.kurt.gym.employee.contoller;

import com.kurt.gym.core.rest.api.util.EmployeeUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kurt.gym.core.persistence.entity.Employee;
import com.kurt.gym.core.services.EmployeeService;
import com.kurt.gym.helper.service.ApiMessage;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RestController
@RequestMapping("employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<?> getEmployees(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("search") String search,
            @RequestParam(required = false) String role) {
                
        if (role == null)
            return employeeService.data(search, size, page - 1);

        return employeeService.data(search, role, size, page - 1);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable Long id) {
        return employeeService.findOne(id);
    }

    @PostMapping
    public ResponseEntity<?> addEmployee(@RequestBody Employee Employee) {
        return employeeService.save(Employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        if (!Objects.equals(id, employee.getId()))
            return ApiMessage.errorResponse("id is not equal to employee payload id");

        if (EmployeeUtil.findById(id) == null)
            return ApiMessage.errorResponse("employee not found");

        return employeeService.save(employee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        return employeeService.deleteById(id);
    }

    // Last Name first then first name
    @GetMapping("/autocomplete")
    public ResponseEntity<?> searchEmployeeByName(@RequestParam("search") String search) {
        return employeeService.getEmployeeCoachAutoComplete(search);
    }
}
