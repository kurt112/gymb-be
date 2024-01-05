package com.kurt.gym.gym.store.service;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kurt.gym.gym.store.model.Store;
import jakarta.transaction.Transactional;

@Transactional
public interface StoreRepository extends JpaRepository<Store, Long>{

}
