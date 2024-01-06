package com.kurt.gym.gym.classes.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kurt.gym.core.persistence.entity.GymClass;
import com.kurt.gym.core.persistence.entity.GymClassType;
import com.kurt.gym.core.services.GymClassService;
import com.kurt.gym.core.services.GymClassWithUserService;
import com.kurt.gym.helper.service.ApiMessage;
import com.kurt.gym.core.persistence.entity.Schedule;
import com.kurt.gym.core.persistence.entity.ScheduleData;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("gym/classes")
public class GymClassController {

    private final GymClassService gymClassService;
    private final GymClassWithUserService gymClassWithUserService;

    @GetMapping
    public ResponseEntity<?> getClasses(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("search") String search) {

        return gymClassService.data(search, size, page - 1);
    }

    @PostMapping
    public ResponseEntity<?> addGymClass(@RequestBody GymClass gymClass) {
        return gymClassService.save(gymClass);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> addGymClass(@PathVariable Long id, @RequestBody GymClass gymClass) {

        if (id != gymClass.getId())
            return ApiMessage.errorResponse("id is not equal to the id in payload");

        if (gymClassService.findOne(id) == null)
            return ApiMessage.errorResponse("Gym class not found");

        return gymClassService.save(gymClass);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable Long id) {
        return gymClassService.findOne(id);
    }

    @PostMapping("/{gymClassId}/enroll-customer/{rfId}")
    public ResponseEntity<?> enrollCustomer(@PathVariable Long gymClassId, @PathVariable String rfId) {
        return gymClassService.enrollCustomer(rfId, gymClassId);
    }

    @PostMapping("/{gymClassId}/un-enroll-customer/{rfId}")
    public ResponseEntity<?> unEnrollCustomer(@PathVariable Long gymClassId, @PathVariable String rfId) {
        return gymClassService.unEnrollGymClassCustomer(rfId, gymClassId);
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<?> getGymClassMembers(@PathVariable long id,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("search") String search) {
        return gymClassService.getGymClassMembers(id, search, size, page - 1);
    }

    @GetMapping("/schedules")
    public ResponseEntity<?> gymClassSchedule() {
        return gymClassService.getGymClasses();
    }

    @GetMapping("/{id}/schedules")
    public ResponseEntity<?> getGymClassSchedule(@PathVariable long id) {
        return gymClassService.getGymClassSchedule(id);
    }

    @PostMapping("/{gymClassId}/schedules")
    public ResponseEntity<?> addGymClassSchedule(@PathVariable("gymClassId") Long id, @RequestBody Schedule schedule) {
        System.out.println(schedule.toString());
        return gymClassService.saveGymClassSchedule(id,schedule);
    }

    @DeleteMapping("/{id}/schedules/{schedule-id}")
    public ResponseEntity<?> deletGymClassSchedule(@PathVariable("id") Long gymClassId,
            @PathVariable("schedule-id") long scheduleId) {
        return gymClassService.deleteGymClassSchedule(gymClassId, scheduleId);
    }

    @PostMapping("/{id}/generate-schedules")
    public ResponseEntity<?> generateGymClassSchedule(@PathVariable Long id,
            @RequestBody List<ScheduleData> schedules) {
        return gymClassService.generateGymClassSchedule(id, schedules);
    }

    @PostMapping("/{gymClassId}/assign-instructor/{instructorId}")
    public ResponseEntity<?> assignInstructor(@PathVariable("gymClassId") Long gymClassId,
            @PathVariable("instructorId") Long instrutorId) {

        return gymClassService.assignGymClassInstructor(gymClassId, instrutorId);
    }

    @PostMapping("/type")
    public ResponseEntity<?> createGymClassType(@RequestBody GymClassType gymClassType) {

        return gymClassService.saveGymClassType(gymClassType);
    }

    @GetMapping("/types")
    public ResponseEntity<?> getGymClassTypes() {

        return gymClassService.getGymClassTypes();
    }

    @GetMapping("/types/{id}")
    public ResponseEntity<?> getGymClassType(@PathVariable("id") Long id) {

        return gymClassService.getGymClassType(id);
    }

    @DeleteMapping("/type")
    public ResponseEntity<?> deleteGymClassType(@RequestParam("id") Long id) {

        return gymClassService.deleteGymClassType(id);
    }
}
