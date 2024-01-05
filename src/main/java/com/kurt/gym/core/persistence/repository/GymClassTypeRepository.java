package com.kurt.gym.core.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kurt.gym.core.persistence.entity.GymClassType;

public interface GymClassTypeRepository extends JpaRepository<GymClassType, Long> {
    
}
