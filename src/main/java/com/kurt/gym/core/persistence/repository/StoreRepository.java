package com.kurt.gym.core.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kurt.gym.core.persistence.entity.Store;
import jakarta.transaction.Transactional;

@Transactional
public interface StoreRepository extends JpaRepository<Store, Long>{

}
