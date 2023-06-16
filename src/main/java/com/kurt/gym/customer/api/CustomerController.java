package com.kurt.gym.customer.api;

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

import com.kurt.gym.customer.model.Customer;
import com.kurt.gym.customer.model.services.CustomerService;
import com.kurt.gym.helper.service.ApiMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<?> getCustomers(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("search") String search) {

        return customerService.data(search, size, page - 1);
    }

    @GetMapping("/today")
    public ResponseEntity<?> getTodayCustomers(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("search") String search) {

        return customerService.getTodaysCustomer(search, size, page - 1);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable Long id) {

        return customerService.findOne(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody Customer customer){

        if(id != customer.getId()) return ApiMessage.errorResponse("Id not equal to id in body");

        if(customerService.referencedById(id) == null) return ApiMessage.errorResponse("Customer not found"); 

        return customerService.save(customer);
    }

    @PostMapping
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer) {
        return customerService.save(customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        return customerService.deleteById(id);
    }

    @GetMapping("attendance/{rfId}")
    public ResponseEntity<?> updateCustomerAttendanceByRfid(@PathVariable String rfId) {
        return customerService.updateCustomerAttendaceByRfId(rfId);
    }
}
