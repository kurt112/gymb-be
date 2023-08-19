package com.kurt.gym.gym.store.service;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kurt.gym.gym.store.model.StoreSale;

public interface StoreSaleRepository extends JpaRepository<StoreSale, Long>{

    StoreSale findStoreSaleByCreatedAt(Date date);
    
}
