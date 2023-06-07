package com.kurt.gym.employee.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.kurt.gym.employee.model.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import jakarta.transaction.Transactional;

@Transactional
public interface EmployeeRepository extends JpaRepository<Employee, Long>{
    @Query("select e.id from Employee e where e.id = ?1")
    Long isExist(Long id);

    Page<Employee> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
