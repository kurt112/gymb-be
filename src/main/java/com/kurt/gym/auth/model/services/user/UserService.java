package com.kurt.gym.auth.model.services.user;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.auth.model.user.User;
import com.kurt.gym.helper.service.BaseService;


@Service
public interface UserService extends BaseService<User> {
    
    ResponseEntity<Page<User>> data(String search,  int size, int page);
    
    ResponseEntity<User> findOne(Long id);
}
