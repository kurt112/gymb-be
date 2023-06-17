package com.kurt.gym.gym.model.classes.service.GymClass;

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
import com.kurt.gym.gym.model.classes.GymClassWithUser;
import com.kurt.gym.gym.model.classes.service.gymClassWithUser.GymClassWithUserRepositoy;
import com.kurt.gym.helper.service.ApiMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GymClassServiceImpl implements GymClassService {

    private final GymClassRepository gymClassRepository;
    private final GymClassWithUserRepositoy gymClassWithUserRepositoy;

    @Override
    @CachePut(cacheNames = { "gymClass" }, key = "#t.id")
    public ResponseEntity<GymClass> save(GymClass t) {
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
        Page<GymClass> classes = gymClassRepository.findAll(pageable);

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

}