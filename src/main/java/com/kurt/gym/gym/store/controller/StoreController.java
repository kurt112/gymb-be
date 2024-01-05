package com.kurt.gym.gym.store.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.kurt.gym.core.persistence.entity.Store;
import com.kurt.gym.core.services.StoreService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import java.util.Date;

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

    @GetMapping("/{id}/dashboard/date-sale/{date}")
    public ResponseEntity<?> getStoreDateSale(@PathVariable Long id,@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return storeService.getDateSale(id, date);
    }


}
