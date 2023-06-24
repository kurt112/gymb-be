package com.kurt.gym.gym.store.controller;

import org.springframework.web.bind.annotation.RestController;

import com.kurt.gym.gym.store.service.StoreService;

import lombok.RequiredArgsConstructor;

@RestController("gym")
@RequiredArgsConstructor
public class StoreControler {

    private final StoreService storeService;
    
}
