package com.kurt.gym.core.rest.api.util;

import com.kurt.gym.core.persistence.entity.Employee;
import com.kurt.gym.core.persistence.repository.EmployeeRepository;
import com.kurt.gym.helper.model.AutoComplete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class EmployeeUtil {
    private static EmployeeRepository employeeRepository;

    public static void initRepositories(EmployeeRepository employeeRepository) {
        EmployeeUtil.employeeRepository = employeeRepository;
    }

    private EmployeeUtil() {

    }

    public static void save(Employee employee) {
        employeeRepository.saveAndFlush(employee);
    }

    public static Employee findById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public static void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }

    public static Employee getReferenceById(Long id) {
        return employeeRepository.getReferenceById(id);
    }

    public static Long countCoach() {
        return employeeRepository.countCoach();
    }

    public static void delete(Employee employee) {
        employeeRepository.delete(employee);
    }

    public static Page<Employee> findAllByOrderByCreatedAtDesc(String search, Pageable pageable) {
        return employeeRepository.findAllByOrderByCreatedAtDesc(search, pageable);
    }

    public static Page<AutoComplete> findEmployeeCoachBySearch(String search, Pageable pageable) {
        return employeeRepository.findEmployeeCoachBySearch(search, pageable);
    }

    public static Page<Employee> findAllEmployeeWithRoleByOrderByCreatedAtDesc(String search, int role, Pageable pageable) {
        return employeeRepository.findAllEmployeeWithRoleByOrderByCreatedAtDesc(search, role, pageable);
    }
}
