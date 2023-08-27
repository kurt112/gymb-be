package com.kurt.gym.gym.store.controller;

import org.springframework.web.bind.annotation.RestController;

import com.kurt.gym.gym.store.model.Store;
import com.kurt.gym.gym.store.service.StoreService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("gym")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<?> saveStoreData(@RequestBody Store store) {
        return storeService.save(store);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStoreData(@PathVariable long id) {
        return storeService.findOne(id);
    }

    @GetMapping("/{id}/dashboard")
    public ResponseEntity<?> getStoreDashboard(@PathVariable long id) {
        return storeService.dashboard(id);
    }

}
