package com.kurt.gym.auth.model.services.user;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kurt.gym.auth.model.user.User;

import jakarta.transaction.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<User,Long> {
    
    @Query("select e.id from User e where e.id = ?1")
    Long isUserExist(Long id);

    @Query("select e from User e where e.id = ?1")
    User getUser(Long id);

    @Query("select e from User e")
    Page<User> getUsers(Pageable pageable);
}