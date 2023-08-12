package com.kurt.gym.gym.classes.service.GymClass;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kurt.gym.gym.classes.model.GymClassType;

public interface GymClassTypeRepository extends JpaRepository<GymClassType, Long> {
    
}
