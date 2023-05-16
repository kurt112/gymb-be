package com.kurt.gym.auth.model.services;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.auth.api.UserGet;
import com.kurt.gym.auth.model.User;
import com.kurt.gym.helper.service.BaseService;


@Service
public interface UserService extends BaseService<User> {
    
    ResponseEntity<Page<UserGet>> data(String search, long size, int page);
    
    ResponseEntity<UserGet> findOne(Long id);
}
