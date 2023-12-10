package com.kurt.gym.config.initData;

import com.kurt.gym.auth.model.user.User;
import com.kurt.gym.auth.model.user.UserRole;
import com.kurt.gym.employee.model.Employee;
import com.kurt.gym.employee.services.EmployeeRepository;
import com.kurt.gym.gym.membership.model.Membership;
import com.kurt.gym.gym.membership.service.MembershipRepository;
import com.kurt.gym.gym.store.model.Store;
import com.kurt.gym.gym.store.service.StoreRepository;
import com.kurt.gym.helper.Charges;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class InitialData {

    private final MembershipRepository membershipRepository;
    private final EmployeeRepository employeeRepository;
    private final StoreRepository storeRepository;
    private final Logger logger = LoggerFactory.getLogger(InitialData.class);

    @PostConstruct
    public void createInitialData() {

        logger.info("Creating Initial Data -----------------------------------------------------");
        final Membership default_membership = membershipRepository.findById(1L).orElse(null);
        final Employee default_employee_admin = employeeRepository.findById(1L).orElse(null);
        final Store default_store = storeRepository.findById(1L).orElse(null);

        if (default_membership == null) {
            Calendar promoExpiration = Calendar.getInstance();
            promoExpiration.add(Calendar.YEAR, 999);
            Membership systemDefaultMembership = Membership
                    .builder()
                    .id(1L)
                    .name("DEFAULT_MEMBERSHIP")
                    .code("DEFAULT")
                    .price(10)
                    .week((short) 9)
                    .year((short) 9)
                    .month((short) 9)
                    .duration(99999999L)
                    .membershipPromoExpiration(promoExpiration.getTime())
                    .durationDescription("This Membership is the default charges when you are not member")
                    .charge(Charges.EVERYDAY)
                    .build();
            membershipRepository.save(systemDefaultMembership);
        }

        if (default_employee_admin == null) {
            // don't delete the data create this is the backbone of the application to run
            User systemInitialAdmin = User
                    .builder()
                    .id(1L)
                    .firstName("admin")
                    .lastName("admin")
                    .email("admin@email.com")
                    .password("password")
                    .birthDate(new Date())
                    .cellphone("09617134338")
                    .cardValue(new BigDecimal(999999))
                    .role(UserRole.ADMIN)
                    .build();

            Employee systemDefaultEmployee = Employee
                    .builder()
                    .id(1L)
                    .user(systemInitialAdmin)
                    .build();

            employeeRepository.save(systemDefaultEmployee);
        }

        if (default_store == null) {
            Store store = Store
                    .builder()
                    .id(1L)
                    .name("Default_Store")
                    .totalSales(new BigDecimal(0))
                    .email("kurtorioque112@gmail.com")
                    .amountNeedToEarnOnePoint(new BigDecimal(30))
                    .build();

            storeRepository.save(store);
        }

        logger.info("Done Create Initial Data -----------------------------------------------------");
    }

}
