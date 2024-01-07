package com.kurt.gym.config.api;

import com.kurt.gym.auth.model.services.user.UserRepository;
import com.kurt.gym.core.persistence.repository.*;
import com.kurt.gym.core.rest.api.util.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ComponentScan("com.kurt.gym.core.persistence.repository")
public class GymRepositoryConfiguration {

    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final StoreSaleRepository storeSaleRepository;
    private final ScheduleRepository scheduleRepository;
    private final GymClassRepository gymClassRepository;
    private final GymClassTypeRepository gymClassTypeRepository;
    @PostConstruct
    public void initRepository(){
        MembershipUtil.initRepositories(this.membershipRepository);
        EmployeeUtil.initRepositories(this.employeeRepository);
        UserUtil.initRepositories(this.userRepository);
        CustomerUtil.initRepositories(this.customerRepository);
        StoreUtil.initRepositories(this.storeRepository, this.storeSaleRepository);
        ScheduleUtil.initRepositories(this.scheduleRepository);
        GymClassUtil.initRepositories(this.gymClassRepository, this.gymClassTypeRepository);
    }
}
