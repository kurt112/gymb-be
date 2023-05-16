package com.kurt.gym.helper.service;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ApiMessage {
    public static ResponseEntity<HashMap<String,String>> errorResponse(String message){

        HashMap<String,String> result = new HashMap<String,String>();

        result.put("message", message);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<HashMap<String,String>> successResponse(String message){

        HashMap<String,String> result = new HashMap<String,String>();

        result.put("message", message);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
