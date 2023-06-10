package com.kurt.gym.gym.model.classes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kurt.gym.gym.model.classes.GymClass;
import com.kurt.gym.gym.model.classes.service.GymClassService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("gym/classes")
public class GymClassController {

    private final GymClassService gymClassService;

    @GetMapping
    public ResponseEntity<?> getClasses(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("search") String search) {

        return gymClassService.data(search, size, page - 1);
    }

    @PostMapping
    public ResponseEntity<?> addGymClass(@RequestBody GymClass gymClass) {
        System.out.println(gymClass.getSchedules().size());
        return gymClassService.save(gymClass);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable Long id){
        return gymClassService.findOne(id);
    }

}
