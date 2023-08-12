package com.kurt.gym.gym.classes.service.GymClass;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.auth.model.user.User;
import com.kurt.gym.customer.model.Customer;
import com.kurt.gym.customer.services.CustomerRepository;
import com.kurt.gym.employee.model.Employee;
import com.kurt.gym.employee.services.EmployeeRepository;
import com.kurt.gym.gym.classes.model.GymClass;
import com.kurt.gym.gym.classes.model.GymClassType;
import com.kurt.gym.gym.classes.model.GymClassWithUser;
import com.kurt.gym.gym.classes.service.gymClassWithUser.GymClassWithUserRepositoy;
import com.kurt.gym.helper.service.ApiMessage;
import com.kurt.gym.schedule.model.Schedule;
import com.kurt.gym.schedule.model.ScheduleData;
import com.kurt.gym.schedule.service.ScheduleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GymClassServiceImpl implements GymClassService {

    private final GymClassRepository gymClassRepository;
    private final GymClassWithUserRepositoy gymClassWithUserRepositoy;
    private final CustomerRepository customerRepository;
    private final ScheduleRepository scheduleRepository;
    private final EmployeeRepository employeeRepository;
    private final GymClassTypeRepository gymClassTypeRepository;

    @Transactional
    @Override
    // @CachePut(cacheNames = { "gymClass" }, key = "#t.id")
    public ResponseEntity<GymClass> save(GymClass t) {

        Calendar dateStart = Calendar.getInstance();

        GymClassType gymClassType = t.getGymClassType();

        t.setGymClassType(null);

        boolean isGymClassActive = true;

        if (t.getDateStart() != null) {
            dateStart.setTime(t.getDateStart());
            isGymClassActive = false;
        }

        Calendar dateEnd = Calendar.getInstance();

        if (t.getDateEnd() != null) {
            dateEnd.setTime(t.getDateEnd());
            isGymClassActive = false;
        }

        t.setIsActive(isGymClassActive);
        gymClassRepository.save(t);

        t.setGymClassType(gymClassType);
        gymClassRepository.save(t);
        return new ResponseEntity<GymClass>(
                t,
                HttpStatus.OK);
    }

    @Override
    @CacheEvict(cacheNames = { "gymClass" }, key = "#t.id")
    public ResponseEntity<?> delete(GymClass t) {
        GymClass gymClass = gymClassRepository.findById(t.getId()).orElse(null);

        if (gymClass == null)
            return ApiMessage.errorResponse("GymClass not found");

        return ApiMessage.successResponse("GymClass deleted");
    }

    @Override
    @CacheEvict(cacheNames = { "gymClass" }, key = "#id")
    public ResponseEntity<HashMap<String, String>> deleteById(Long id) {

        GymClass gymClass = gymClassRepository.findById(id).orElse(null);

        if (gymClass == null)
            return ApiMessage.errorResponse("GymClass not found");

        return ApiMessage.successResponse("GymClass deleted");
    }

    @Override
    public ResponseEntity<Page<GymClass>> data(String search, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GymClass> classes = gymClassRepository.getGymClassWithoutSchedules(pageable);

        return new ResponseEntity<>(classes, HttpStatus.OK);
    }

    @Override
    @Cacheable(cacheNames = "gymClass", key = "#id")
    public ResponseEntity<?> findOne(Long id) {
        GymClass gymClass = gymClassRepository.findById(id).orElse(null);

        if (gymClass == null)
            return ApiMessage.errorResponse("Gym class not found");

        return new ResponseEntity<GymClass>(
                gymClass,
                HttpStatus.OK);
    }

    @Override
    public GymClass referencedById(Long id) {
        return gymClassRepository.getReferenceById(id);
    }

    @Override
    public ResponseEntity<?> getGymClassMembers(long gymClassId, String search, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GymClassWithUser> gymClassWithUser = gymClassWithUserRepositoy.getGymClassMembers(gymClassId, pageable);

        return new ResponseEntity<>(gymClassWithUser, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> enrollCustomer(String rfId, long gymClassId) {

        Long customerId = customerRepository.findCustomerIdByRfID(rfId);

        if (customerId == null) {
            return ApiMessage.errorResponse("No Customer Found");
        }

        GymClass gymClass = gymClassRepository.getReferenceById(gymClassId);

        if (gymClass == null) {
            return ApiMessage.errorResponse("Gym Class Not Found");
        }

        Customer customer = customerRepository.getReferenceById(customerId);

        if (!gymClass.getAllowedNonMembers() && !customer.getIsMember()) {
            return ApiMessage.errorResponse("Customer is not subscribed to any membership");
        }

        Long gymClassWithUserId = gymClassWithUserRepositoy.getGymClassWithUser(gymClassId, customer.getUser().getId());

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

        gymClassWithUserRepositoy.save(gymClassWithUser);

        return ApiMessage.successResponse("Customer successfully enrolled");
    }

    @Override
    public ResponseEntity<?> unEnrollGymClassCustomer(String rfId, long gymClassId) {

        Long customerId = customerRepository.findCustomerIdByRfID(rfId);

        if (customerId == null) {
            return ApiMessage.errorResponse("No Customer Found");
        }

        Customer customer = customerRepository.getReferenceById(customerId);

        Long gymClassWithUserId = gymClassWithUserRepositoy.getGymClassWithUser(gymClassId, customer.getUser().getId());

        if (gymClassWithUserId == null)
            return ApiMessage.errorResponse("Customer not currently enrolled in this class");

        GymClassWithUser gymClassWithUser = gymClassWithUserRepositoy.getReferenceById(gymClassWithUserId);

        gymClassWithUser.setIsActive(false);

        gymClassWithUserRepositoy.save(gymClassWithUser);

        return ApiMessage.successResponse("Customer successfully remove from this class");
    }

    @Override
    public ResponseEntity<?> getGymClasses() {
        List<GymClass> list = gymClassRepository.getGymClassesSchedule();

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<?> generateGymClassSchedule(long gymClassId, List<ScheduleData> scheduleDatas) {

        GymClass currentGymClass = gymClassRepository.findById(gymClassId).orElse(null);

        if (currentGymClass == null) {
            return ApiMessage.errorResponse("No Gym Class Found");
        }

        // Looping the scheudle data so that we can assure the arrange ment of scheyudle
        // with day

        HashMap<Short, ScheduleData> scheduleMap = new HashMap<>();

        for (ScheduleData scheduleData : scheduleDatas) {
            scheduleMap.put(scheduleData.getDay(), scheduleData);
        }

        List<Schedule> newSchedules = new ArrayList<>();

        Calendar dateStart = Calendar.getInstance();
        dateStart.setTime(currentGymClass.getDateStart());

        Calendar dateEnd = Calendar.getInstance();
        dateEnd.setTime(currentGymClass.getDateEnd());

        dateEnd.add(Calendar.DAY_OF_WEEK, 1);

        currentGymClass.getSchedules().forEach(e -> {
            this.scheduleRepository.deleteScheduleById(e.getId());
        });

        while (dateStart.before(dateEnd)) {

            int currentDay = dateStart.get(Calendar.DAY_OF_WEEK);

            // in js the Sunday the inedex is zero so we minus 1
            short possibleDaySched = (short) (currentDay - 1);

            ScheduleData scheduleData = scheduleMap.get(possibleDaySched);

            if (scheduleData == null) {
                dateStart.add(Calendar.DAY_OF_WEEK, 1);
                continue;
            }

            Date startDateWithTime = scheduleData.getStartTime();
            Date endDateWithTime = scheduleData.getEndTime();

            Calendar startDateWithTimeCalendar = Calendar.getInstance();
            startDateWithTimeCalendar.setTime(startDateWithTime);
            startDateWithTimeCalendar.set(dateStart.get(Calendar.YEAR),
                    dateStart.get(Calendar.MONTH),
                    dateStart.get(Calendar.DAY_OF_MONTH));

            Calendar endDateWithTimeCalendar = Calendar.getInstance();
            endDateWithTimeCalendar.setTime(endDateWithTime);
            endDateWithTimeCalendar.set(dateStart.get(Calendar.YEAR),
                    dateStart.get(Calendar.MONTH),
                    dateStart.get(Calendar.DAY_OF_MONTH));

            Schedule schedule = Schedule
                    .builder()
                    .endTime(endDateWithTimeCalendar.getTime())
                    .startTime(startDateWithTimeCalendar.getTime())
                    .gymClass(currentGymClass)
                    .build();

            newSchedules.add(schedule);
            scheduleRepository.save(schedule);

            dateStart.add(Calendar.DAY_OF_WEEK, 1);
        }

        return ApiMessage.successResponse("Generated schedule success");
    }

    @Override
    public ResponseEntity<?> getGymClassSchedule(long gymClassId) {
        List<Schedule> list = scheduleRepository.getGymClassSchedules(gymClassId);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> assignGymClassInstructor(long gymClassId, long instructorId) {

        GymClass gymClass = this.gymClassRepository.getReferenceById(gymClassId);

        if (gymClass == null)
            return ApiMessage.errorResponse("Can't find gym class with id of " + gymClassId);

        Employee employee = this.employeeRepository.getReferenceById(instructorId);

        if (employee == null)
            return ApiMessage.errorResponse("Can't find employee with id of " + instructorId);

        User user = employee.getUser();

        String instructorName = user.getLastName() + ", " + user.getFirstName();

        gymClass.setInstructor(employee);
        gymClass.setInstructorName(instructorName);

        this.gymClassRepository.save(gymClass);

        return ApiMessage.successResponse("Assigne insturctor success");
    }

    @Override
    public ResponseEntity<?> saveGymClassType(GymClassType gymClassType) {

        gymClassTypeRepository.save(gymClassType);

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
    public ResponseEntity<?> getGymClassTypes() {
        List<GymClassType> gymClassTypes = gymClassTypeRepository.findAll();

        return new ResponseEntity<List<GymClassType>>(
                gymClassTypes,
                HttpStatus.OK);
    }

    @Override
    @Modifying
    public ResponseEntity<?> deleteGymClassType(Long id) {
        System.out.println(id);
        gymClassTypeRepository.deleteById(id);

        return ApiMessage.successResponse("Gym class type deleted successfully");
    }

}
