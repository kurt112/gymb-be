package com.kurt.gym.gym.model.store.service;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.gym.model.store.Store;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StoreImpl implements StoreService {
    
    private final StoreRepository storeRepository;

    @Override
    public ResponseEntity<HashMap<String, String>> save(Store t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public ResponseEntity<HashMap<String, String>> delete(Store t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public ResponseEntity<HashMap<String, String>> deleteById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public Long isExist(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isExist'");
    }


    
}
