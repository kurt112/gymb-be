package com.kurt.gym.config.api;

import com.kurt.gym.auth.model.services.user.UserRepository;
import com.kurt.gym.core.persistence.repository.CustomerRepository;
import com.kurt.gym.core.persistence.repository.EmployeeRepository;
import com.kurt.gym.core.persistence.repository.MembershipRepository;
import com.kurt.gym.core.rest.api.util.CustomerUtil;
import com.kurt.gym.core.rest.api.util.EmployeeUtil;
import com.kurt.gym.core.rest.api.util.MembershipUtil;
import com.kurt.gym.core.rest.api.util.UserUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GymRepositoryConfiguration {

    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public GymRepositoryConfiguration(MembershipRepository membershipRepository, UserRepository userRepository, EmployeeRepository employeeRepository, CustomerRepository customerRepository) {
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
    }

    @PostConstruct
    public void initRepository(){
        MembershipUtil.initRepositories(this.membershipRepository);
        EmployeeUtil.initRepositories(this.employeeRepository);
        UserUtil.initRepositories(this.userRepository);
        CustomerUtil.initRepositories(this.customerRepository);
    }
}
