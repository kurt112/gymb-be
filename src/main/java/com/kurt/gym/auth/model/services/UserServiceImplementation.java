package com.kurt.gym.auth.model.services;

import java.util.Date;
import java.util.HashMap;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kurt.gym.auth.api.UserGet;
import com.kurt.gym.auth.model.User;
import com.kurt.gym.helper.service.ApiMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImplementation implements UserService{

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<HashMap<String, String>> save(User t) {

        userRepository.save(t);
        
        return ApiMessage.successResponse("User saved successfully");
    }

    @Override
    public ResponseEntity<HashMap<String, String>> delete(User t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public ResponseEntity<Page<UserGet>> data(String search, long size, int page) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'data'");
    }

    @Override
    public Long isExist(Long id) {

        return userRepository.isUserExist(id);
    }

    @Override
    public ResponseEntity<UserGet> findOne(Long id) {
        UserGet userGet = userRepository.getUser(id);
        return new ResponseEntity<>(userGet,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HashMap<String, String>> deleteById(Long id) {
        if(isExist(id) == null){
            return ApiMessage.errorResponse("User does not exist");
        }
        
        userRepository.deleteById(id);

        return ApiMessage.successResponse("User deleted successfully");
    }
    
}
