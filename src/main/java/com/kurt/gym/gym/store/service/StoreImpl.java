package com.kurt.gym.gym.store.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.gym.store.Store;
import com.kurt.gym.helper.service.ApiMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StoreImpl implements StoreService {
    
    private final StoreRepository storeRepository;

    @Override
    public ResponseEntity<HashMap<String, String>> save(Store t) {
        this.storeRepository.save(t);

        return ApiMessage.successResponse("Store Data Saved Successfully");
    }

    @Override
    public ResponseEntity<HashMap<String, String>> delete(Store t) {
        this.storeRepository.delete(t);

        return ApiMessage.successResponse("Store Data Deleted Successfully");
    }

    @Override
    public ResponseEntity<HashMap<String, String>> deleteById(Long id) {
       return ApiMessage.successResponse("Store Data Deleted Successfully");
    }

    @Override
    public ResponseEntity<?> data(String search, int size, int page) {
        
        List<Store> stores = this.storeRepository.findAll();
        
        return new ResponseEntity<List<Store>>(stores, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> findOne(Long id) {
         Store store = this.storeRepository.findById(id).orElse(null);

        if (store == null)
            return ApiMessage.errorResponse("Customer not found");

        return new ResponseEntity<Store>(
                store,
                HttpStatus.OK);
    }

    @Override
    public Store referencedById(Long id) {
        return storeRepository.getReferenceById(id);
    }


}
