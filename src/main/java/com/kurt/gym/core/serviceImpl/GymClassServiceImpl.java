package com.kurt.gym.core.serviceImpl;

import com.kurt.gym.auth.model.user.User;
import com.kurt.gym.core.persistence.entity.*;
import com.kurt.gym.core.persistence.repository.*;
import com.kurt.gym.core.rest.api.util.GymClassUtil;
import com.kurt.gym.core.rest.api.util.ScheduleUtil;
import com.kurt.gym.core.services.GymClassService;
import com.kurt.gym.helper.logger.LoggerUtil;
import com.kurt.gym.helper.service.ApiMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class GymClassServiceImpl implements GymClassService {

    private final GymClassWithUserRepository gymClassWithUserRepository;
    private final CustomerRepository customerRepository;
    private final ScheduleRepository scheduleRepository;
    private final EmployeeRepository employeeRepository;
    private final GymClassTypeRepository gymClassTypeRepository;

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "gym-class-data", allEntries = true),
            @CacheEvict(cacheNames = {"gymClass"}, key = "#t.id")
    })
    public ResponseEntity<GymClass> save(GymClass t) {

        Calendar dateStart = Calendar.getInstance();

        GymClassType gymClassType = t.getGymClassType();


        if (t.getDateStart() != null) {
            dateStart.setTime(t.getDateStart());
        }

        Calendar dateEnd = Calendar.getInstance();

        if (t.getDateEnd() != null) {
            dateEnd.setTime(t.getDateEnd());
        }

        boolean isGymClassActive = t.getDateStart() != null && t.getDateEnd() != null;

        t.setGymClassType(null);
        t.setIsActive(isGymClassActive);
        GymClassUtil.save(t);

        if (t.getSchedules() != null && !t.getSchedules().isEmpty()) {
            System.out.println("i am saving");
            for(Schedule schedule: t.getSchedules()){
                if (schedule.getGymClass() == null) {
                    schedule.setGymClass(t);
                    ScheduleUtil.save(schedule);
                }
            }
        }

        t.setGymClassType(gymClassType);
        GymClassUtil.save(t);


        return new ResponseEntity<>(
                t,
                HttpStatus.OK);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = {"gymClass"}, key = "#t.id"),
            @CacheEvict(cacheNames = "gym-class-data", allEntries = true)
    })
    public ResponseEntity<?> delete(GymClass t) {
        GymClass gymClass = GymClassUtil.findById(t.getId());

        if (gymClass == null)
            return ApiMessage.errorResponse("GymClass not found");

        return ApiMessage.successResponse("GymClass deleted");
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = {"gymClass"}, key = "#id"),
            @CacheEvict(cacheNames = "gym-class-data", allEntries = true)
    })
    public ResponseEntity<HashMap<String, String>> deleteById(Long id) {

        GymClass gymClass = GymClassUtil.findById(id);

        if (gymClass == null)
            return ApiMessage.errorResponse("GymClass not found");

        GymClassUtil.deleteById(id);

        return ApiMessage.successResponse("GymClass deleted");
    }

    @Override
    @Cacheable(value = "gym-class-data", key = "new org.springframework.cache.interceptor.SimpleKey(#search, #size, #page)")
    public ResponseEntity<Page<GymClass>> data(String search, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GymClass> classes = GymClassUtil.getGymClassWithoutSchedulesPagable(search, pageable);

        return new ResponseEntity<>(classes, HttpStatus.OK);
    }

    @Override
    @Cacheable(cacheNames = "gymClass", key = "#id")
    public ResponseEntity<?> findOne(Long id) {
        GymClass gymClass = GymClassUtil.findById(id);

        if (gymClass == null)
            return ApiMessage.errorResponse("Gym class not found");

        return new ResponseEntity<>(
                gymClass,
                HttpStatus.OK);
    }

    public GymClass referencedById(Long id) {
        return GymClassUtil.getReferenceById(id);
    }

    @Override
    @Cacheable(value = "gym-class-members", key = "#gymClassId")
    public ResponseEntity<?> getGymClassMembers(long gymClassId, String search, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GymClassWithUser> gymClassWithUser = gymClassWithUserRepository.getGymClassMembers(gymClassId, pageable);

        return new ResponseEntity<>(gymClassWithUser, HttpStatus.OK);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "gym-class-members", key = "#gymClassId")
    })
    public ResponseEntity<?> enrollCustomer(String rfId, long gymClassId) {

        Long customerId = customerRepository.findCustomerIdByRfID(rfId);

        if (customerId == null) {
            return ApiMessage.errorResponse("No Customer Found");
        }

        GymClass gymClass = GymClassUtil.getReferenceById(gymClassId);

        if (gymClass == null) {
            return ApiMessage.errorResponse("Gym Class Not Found");
        }

        Customer customer = customerRepository.getReferenceById(customerId);

        if (!gymClass.getAllowedNonMembers() && !customer.getIsMember()) {
            return ApiMessage.errorResponse("Customer is not subscribed to any membership");
        }

        Long gymClassWithUserId = gymClassWithUserRepository.getGymClassWithUser(gymClassId, customer.getUser().getId());

        if (gymClassWithUserId != null) {
            return ApiMessage.errorResponse("Customer already enrolled in this class");
        }

        GymClassWithUser gymClassWithUser = GymClassWithUser.builder()
                .currentEnroll(customer.getUser())
                .dateStart(new Date())
                .gymClass(gymClass)
                .session(gymClass.getSession())
                .isActive(true)
                .build();

        gymClassWithUserRepository.save(gymClassWithUser);

        return ApiMessage.successResponse("Customer successfully enrolled");
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "gym-class-members", key = "#gymClassId")
    })
    public ResponseEntity<?> unEnrollGymClassCustomer(String rfId, long gymClassId) {

        Long customerId = customerRepository.findCustomerIdByRfID(rfId);

        if (customerId == null) {
            return ApiMessage.errorResponse("No Customer Found");
        }

        Customer customer = customerRepository.getReferenceById(customerId);

        Long gymClassWithUserId = gymClassWithUserRepository.getGymClassWithUser(gymClassId, customer.getUser().getId());

        if (gymClassWithUserId == null)
            return ApiMessage.errorResponse("Customer not currently enrolled in this class");

        GymClassWithUser gymClassWithUser = gymClassWithUserRepository.getReferenceById(gymClassWithUserId);

        gymClassWithUser.setIsActive(false);

        gymClassWithUserRepository.save(gymClassWithUser);

        return ApiMessage.successResponse("Customer successfully remove from this class");
    }

    @Override
    @Cacheable(value = "gym-class-schedules-all")
    public ResponseEntity<?> getGymClasses() {
        List<GymClass> list = GymClassUtil.getGymClassesSchedule();

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "gym-class-schedules", key = "#gymClassId"),
            @CacheEvict(value = "gym-class-schedules-all", allEntries = true)
    })
    public ResponseEntity<?> generateGymClassSchedule(long gymClassId, List<ScheduleData> scheduleDatas) {

        GymClass currentGymClass = GymClassUtil.findById(gymClassId);

        GymClassUtil.generateSchedule(currentGymClass, scheduleDatas);

        if (currentGymClass == null) {
            return ApiMessage.errorResponse("No Gym Class Found");
        }


        return ApiMessage.successResponse("Generated schedule success");
    }

    @Override
    @Cacheable(value = "gym-class-schedules", key = "#gymClassId")
    public ResponseEntity<?> getGymClassSchedule(long gymClassId) {
        List<Schedule> list = scheduleRepository.getGymClassSchedules(gymClassId);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> assignGymClassInstructor(long gymClassId, long instructorId) {

        GymClass gymClass = GymClassUtil.getReferenceById(gymClassId);

        if (gymClass == null)
            return ApiMessage.errorResponse("Can't find gym class with id of " + gymClassId);

        Employee employee = this.employeeRepository.getReferenceById(instructorId);

        if (employee == null)
            return ApiMessage.errorResponse("Can't find employee with id of " + instructorId);

        User user = employee.getUser();

        String instructorName = user.getLastName() + ", " + user.getFirstName();

        gymClass.setInstructor(employee);
        gymClass.setInstructorName(instructorName);

        GymClassUtil.save(gymClass);

        return ApiMessage.successResponse("Assign instructor success");
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "gym-class-types", allEntries = true)
    })
    public ResponseEntity<?> saveGymClassType(GymClassType gymClassType) {

        System.out.println("i am here");
        gymClassTypeRepository.save(gymClassType);
//        Objects.requireNonNull(cacheManager.getCache("gym-class-types")).clear();
        return new ResponseEntity<GymClassType>(
                gymClassType,
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getGymClassType(Long id) {

        GymClassType gymClassType = gymClassTypeRepository.findById(id).orElse(null);

        if (gymClassType == null)
            return ApiMessage.errorResponse("No Gym Class Type found with id " + id);

        return new ResponseEntity<GymClassType>(
                gymClassType,
                HttpStatus.OK);
    }

    @Override
    @Cacheable("gym-class-types")
    public ResponseEntity<?> getGymClassTypes() {
        List<GymClassType> gymClassTypes = gymClassTypeRepository.findAll();

        return new ResponseEntity<>(
                gymClassTypes,
                HttpStatus.OK);
    }

    @Override
    @Modifying
    @Caching(evict = {
            @CacheEvict(value = "gym-class-types", allEntries = true)
    })
    public ResponseEntity<?> deleteGymClassType(Long id) {
        gymClassTypeRepository.deleteById(id);
        return ApiMessage.successResponse("Gym class type deleted successfully");
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "gym-class-schedules", key = "#gymClassId"),
            @CacheEvict(value = "gym-class-schedules-all", allEntries = true)
    })
    public ResponseEntity<?> deleteGymClassSchedule(Long gymClassId, Long scheduleId) {
        Schedule schedule = scheduleRepository.getReferenceById(scheduleId);

        if (schedule == null)
            return ApiMessage.errorResponse("Schedule not found");

        if (schedule.getGymClass().getId() != gymClassId)
            return ApiMessage.errorResponse("Schedule does not exsit in current gym class");

        scheduleRepository.deleteScheduleById(scheduleId);

        return ApiMessage.successResponse("Schedule deleted successfully");
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "gym-class-schedules", key = "#gymClassId"),
            @CacheEvict(value = "gym-class-schedules-all", allEntries = true)
    })
    public ResponseEntity<?> saveGymClassSchedule(Long gymClassId, Schedule schedule) {

        GymClass gymClass = GymClassUtil.getReferenceById(gymClassId);

        Schedule existingSchedule = scheduleRepository.findById(schedule.getId()).orElse(null);

        if (existingSchedule == null || existingSchedule.getGymClass().getId() != gymClass.getId()) {
            return ApiMessage.errorResponse("Schedule does not exist in current gym class");
        }

        if (schedule != null) {
            existingSchedule.setStartTime(schedule.getStartTime());
            existingSchedule.setEndTime(schedule.getEndTime());
            scheduleRepository.saveAndFlush(existingSchedule);
        }

        return ApiMessage.successResponse("Schedule saved successfully");
    }

}
