package com.kurt.gym.core.rest.api.util;

import com.kurt.gym.core.persistence.entity.Customer;
import com.kurt.gym.core.persistence.repository.CustomerRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class CustomerUtil {
    private static CustomerRepository customerRepository;

    CustomerUtil() {
    }

    public static void initRepositories(CustomerRepository customerRepository) {
        CustomerUtil.customerRepository = customerRepository;
    }

    public static Customer save(Customer customer) {
        customerRepository.save(customer);
        return customer;
    }

    public static Customer findById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public static Customer getReferenceById(Long id) {
        return customerRepository.getReferenceById(id);
    }

    public static void deleteById(Long id) {
        customerRepository.deleteById(id);
    }


    public static void delete(Customer customer) {
        customerRepository.delete(customer);
    }

   public static Page<Customer> findAllByOrderByCreatedAtDesc(String search, Pageable pageable) {
        return customerRepository.findAllByOrderByCreatedAtDesc(search, pageable);
    }

    public static Long findCustomerIdByRfID(String rfId) {
        return customerRepository.findCustomerIdByRfID(rfId);
    }

    public static Page<Customer> todayCustomer(Pageable pageable) {
        return customerRepository.todayCustomer(Instant.now(),pageable);
    }

    public static Customer findCustomerByFirstNameLastNameAndMiddleName(String firstName, String lastName, String middleName) {
        return customerRepository.findCustomerByFirstNameLastNameAndMiddleName(firstName, lastName, middleName);
    }

    public static Long countCustomer() {
        return customerRepository.count();
    }

}
