package com.kurt.gym.gym.model.classes.service;

import java.util.HashMap;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.gym.model.classes.GymClass;
import com.kurt.gym.helper.service.ApiMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GymClassServiceImpl implements GymClassService {

    private final GymClassRepository gymClassRepository;

    @Override
    @CachePut(cacheNames = { "gymClass" }, key = "#t.id")
    public ResponseEntity<HashMap<String, String>> save(GymClass t) {
        System.out.println(t.getName());
        System.out.println(t.getDateStart());
        System.out.println(t.getName());
        System.out.println(t.getSchedules());
        
        gymClassRepository.save(t);
        return ApiMessage.successResponse("Gym Class saved successfully");
    }

    @Override
    @CacheEvict(cacheNames = { "gymClass" }, key = "#t.id")
    public ResponseEntity<HashMap<String, String>> delete(GymClass t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    @CacheEvict(cacheNames = { "gymClass" }, key = "#id")
    public ResponseEntity<HashMap<String, String>> deleteById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public Long isExist(Long id) {
        return gymClassRepository.isExist(id);
    }

    @Override
    public ResponseEntity<Page<GymClass>> data(String search, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GymClass> classes = gymClassRepository.getGymClassWithoutSchedules(pageable);

        return new ResponseEntity<>(classes, HttpStatus.OK);
    }

    @Override
    @Cacheable(cacheNames = "gymClass", key = "#id")
    public ResponseEntity<GymClass> findOne(Long id) {
        GymClass gymCLassFromDb = gymClassRepository.getReferenceById(id);
        return new ResponseEntity<GymClass>(
                GymClass.buildGymClassFromReference(gymCLassFromDb),
                HttpStatus.OK);
    }

}
