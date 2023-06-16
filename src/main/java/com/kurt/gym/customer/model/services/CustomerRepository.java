package com.kurt.gym.customer.model.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kurt.gym.customer.model.Customer;

import jakarta.transaction.Transactional;

@Transactional
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("select e.id from Customer e where e.id = ?1")
    Long isCUstomerExist(Long id);

    @Query("select e.id from Customer e where e.rfID = ?1")
    Long findCustomerIdByRfID(String rfID);

    Page<Customer> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("select new com.kurt.gym.customer.model.Customer(e.id, e.user, e.timeIn, e.timeOut, e.membershipDuration) from Customer e where e.timeIn >= CURRENT_DATE")
    Page<Customer> todaysCustomer(Pageable pageable);
}
