package com.kurt.gym.auth.model.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kurt.gym.auth.api.UserGet;
import com.kurt.gym.auth.model.User;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    
    @Query("select e.id from User e where e.id = ?1")
    Long isUserExist(Long id);

    @Query("select new com.kurt.gym.auth.api.UserGet(e.id, e.firstName, e.lastName, e.email, e.password, e.cellphone, e.gender, e.birthDate) FROM User e where e.id = ?1")
    UserGet getUser(Long id);
}