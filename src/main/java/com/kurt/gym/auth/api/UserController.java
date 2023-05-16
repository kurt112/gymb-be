package com.kurt.gym.auth.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kurt.gym.auth.model.User;
import com.kurt.gym.auth.model.services.UserService;
import com.kurt.gym.helper.service.ApiMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id){
        if(userService.isExist(id) == null){
            return ApiMessage.errorResponse("User Not Found");
        }

        return userService.findOne(id);
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody User user) {
        return userService.save(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        return userService.deleteById(id);
    }

}
