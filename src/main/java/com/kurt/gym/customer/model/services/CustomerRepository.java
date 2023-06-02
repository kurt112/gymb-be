package com.kurt.gym.customer.model.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kurt.gym.customer.model.Customer;

import jakarta.transaction.Transactional;

@Transactional
public interface CustomerRepository extends JpaRepository<Customer, Long>  {
    @Query("select e.id from Customer e where e.id = ?1")
    Long isCUstomerExist(Long id);

}