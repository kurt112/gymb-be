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

@Component
public  class CustomerUtil{
    private static CustomerRepository customerRepository;
    CustomerUtil (){}

    public static void initRepositories(CustomerRepository customerRepository) {
        CustomerUtil.customerRepository = customerRepository;
    }

    @CachePut(cacheNames = { "customer" }, key = "#customer.id")
    @CacheEvict(cacheNames = { "customers-table-data" }, allEntries = true)
    public static Customer save(Customer customer) {
        customerRepository.save(customer);
        return customer;
    }

    @Cacheable(cacheNames = "customer", key = "#id")
    public static Customer findById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Cacheable(cacheNames = "customer-reference-id", key = "#id")
    public static Customer getReferenceById(Long id){
        return customerRepository.getReferenceById(id);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = { "customer" }, key = "#id"),
            @CacheEvict(cacheNames = { "customers-table-data" }, allEntries = true),
            @CacheEvict(cacheNames = { "customer-reference-rfId" }, allEntries = true)
    })
    public static void deleteById(Long id){
        customerRepository.deleteById(id);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = { "customer" }, key = "#customer.id"),
            @CacheEvict(cacheNames = { "customers-table-data" }, allEntries = true),
            @CacheEvict(cacheNames = { "customer-reference-rfId" }, key = "#customer.rfId")
    })
    public static void delete(Customer customer){
        customerRepository.delete(customer);
    }

    @Cacheable(value = "customers-table-data", key = "new org.springframework.cache.interceptor.SimpleKey(#search, #size, #page)")
    public static Page<Customer> findAllByOrderByCreatedAtDesc(String search, Pageable pageable){
        return customerRepository.findAllByOrderByCreatedAtDesc(search, pageable);
    }

    @Cacheable(value = "customer-reference-rfId", key = "#rfId")
    public static Long findCustomerIdByRfID(String rfId){
        return customerRepository.findCustomerIdByRfID(rfId);
    }

    public static Page<Customer> todayCustomer(Pageable pageable){
        return customerRepository.todayCustomer(pageable);
    }

    public static Customer findCustomerByFirstNameLastNameAndMiddleName(String firstName, String lastName, String middleName){
        return customerRepository.findCustomerByFirstNameLastNameAndMiddleName(firstName,lastName,middleName);
    }

    public static Long countCustomer(){
        return customerRepository.count();
    }

}
