package com.kurt.gym.employee.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kurt.gym.employee.model.Employee;
import com.kurt.gym.employee.services.EmployeeService;
import com.kurt.gym.helper.service.ApiMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<?> getEmployees(
        @RequestParam("page") int page, 
        @RequestParam("size") int size, 
        @RequestParam("search") String search) {
        

        return employeeService.data(search, size, page -1);
    }

   @GetMapping("/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable Long id){
        if(employeeService.isExist(id) == null){
            return ApiMessage.errorResponse("Employee Not Found");
        }

        return employeeService.findOne(id);
    }

    @PostMapping
    public ResponseEntity<?> addEmployee(@RequestBody Employee Employee) {
        return employeeService.save(Employee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id){
        return employeeService.deleteById(id);
    }
}
