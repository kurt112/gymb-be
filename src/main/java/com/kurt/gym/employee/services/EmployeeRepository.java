package com.kurt.gym.employee.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kurt.gym.employee.model.Employee;

import jakarta.transaction.Transactional;

@Transactional
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{
    @Query("select e.id from Employee e where e.id = ?1")
    Long isExist(Long id);
}
