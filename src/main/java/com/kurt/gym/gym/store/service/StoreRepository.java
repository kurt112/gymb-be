package com.kurt.gym.gym.store.service;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kurt.gym.gym.store.model.Store;
import com.kurt.gym.gym.store.model.StoreSale;

import jakarta.transaction.Transactional;

@Transactional
public interface StoreRepository extends JpaRepository<Store, Long>{

}
