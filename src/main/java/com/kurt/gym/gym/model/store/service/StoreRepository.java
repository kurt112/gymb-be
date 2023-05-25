package com.kurt.gym.gym.model.store.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kurt.gym.gym.model.store.Store;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface StoreRepository extends JpaRepository<Store, Long>{
    
}
