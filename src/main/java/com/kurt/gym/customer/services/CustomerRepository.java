package com.kurt.gym.customer.services;

import com.kurt.gym.customer.model.CustomerStatus;
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

    @Query("select e.id from Customer e where e.rfId = ?1")
    Long findCustomerIdByRfID(String rfId);
    
    @Query("select new com.kurt.gym.customer.model.Customer(e.id, new com.kurt.gym.auth.model.user.User(e.user.firstName,e.user.lastName, e.user.birthDate, e.user.sex, e.user.cellphone,e.user.email), e.status) " +
    "from Customer e where (e.user.sex like %?1% or e.user.firstName like %?1% or e.user.lastName like %?1% or e.user.cellphone like %?1% or e.user.email like %?1%) order by e.createdAt desc")
    Page<Customer> findAllByOrderByCreatedAtDesc(String search,Pageable pageable);

    @Query("select new com.kurt.gym.customer.model.Customer(e.id, new com.kurt.gym.auth.model.user.User(e.user.firstName,e.user.lastName, e.user.pointsAmount, e.user.cardValue), e.timeIn, e.timeOut, e.membershipDuration) from Customer e where e.timeIn >= CURRENT_DATE")
    Page<Customer> todaysCustomer(Pageable pageable);

    @Query("select e from Customer e where e.user.firstName like %?1% and e.user.lastName like %?2% and e.user.middleName like %?3%")
    Customer findCustomerByFirstNameLastNameAndMiddleName(String firstName, String lastName, String middleName);
}
