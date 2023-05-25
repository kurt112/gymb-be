package com.kurt.gym.auth.model.services.user;

import java.util.HashMap;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.auth.model.user.User;
import com.kurt.gym.helper.service.ApiMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
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
    public ResponseEntity<Page<User>> data(String search, int size, int page) {
        Pageable pageable = PageRequest.of(page,size);
        Page<User> users = userRepository.findAll(pageable);
        
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Override
    public Long isExist(Long id) {

        return userRepository.isUserExist(id);
    }

    @Override
    public ResponseEntity<User> findOne(Long id) {
        User userGet = userRepository.getUser(id);
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
