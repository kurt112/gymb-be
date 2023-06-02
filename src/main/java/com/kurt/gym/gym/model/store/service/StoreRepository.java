package com.kurt.gym.gym.model.store.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kurt.gym.gym.model.store.Store;

import jakarta.transaction.Transactional;

@Transactional
public interface StoreRepository extends JpaRepository<Store, Long>{
    
}
