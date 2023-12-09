package com.kurt.gym.employee.services;

import com.kurt.gym.auth.model.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.kurt.gym.employee.model.Employee;
import com.kurt.gym.helper.model.AutoComplete;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import jakarta.transaction.Transactional;

@Transactional
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
        @Query("select e.id from Employee e where e.id = ?1")
        Long isExist(Long id);

        @Query("select new com.kurt.gym.employee.model.Employee(e.id, new com.kurt.gym.auth.model.user.User(e.user.firstName,e.user.lastName, e.user.birthDate, e.user.sex, e.user.cellphone,e.user.email, e.user.role)) "
                        +
                        "from Employee e where (e.user.sex like %?1% or e.user.firstName like %?1% or e.user.lastName like %?1% or e.user.cellphone like %?1% or e.user.email like %?1%) order by e.createdAt desc")
        Page<Employee> findAllByOrderByCreatedAtDesc(String search, Pageable pageable);

        @Query("select new com.kurt.gym.helper.model.AutoComplete(e.id, CONCAT(e.user.lastName, ', ' , e.user.firstName)) from Employee e where e.user.role = 2 and CONCAT(e.user.lastName, ',' , e.user.firstName) like %?1%")
        Page<AutoComplete> findEmployeeCoachBySearch(String search, Pageable pageable);

        @Query("select new com.kurt.gym.employee.model.Employee(e.id, new com.kurt.gym.auth.model.user.User(e.user.firstName,e.user.lastName, e.user.birthDate, e.user.sex, e.user.cellphone,e.user.email, e.user.role)) "
                        +
                        "from Employee e where (e.user.role like %?1% or e.user.sex like %?1% or e.user.firstName like %?1% or e.user.lastName like %?1% or e.user.cellphone like %?1% or e.user.email like %?1%) and e.user.role =?2 order by e.createdAt desc")
        Page<Employee> findAllEmployeeWithRoleByOrderByCreatedAtDesc(String search, int role, Pageable pageable);

        // In UserRole.java the value of coach is 2
        @Query("select count(e.id) from Employee e where e.user.role = 2")
        Long countCoach();

}
