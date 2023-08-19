package com.kurt.gym.gym.store.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurt.gym.gym.store.model.Store;
import com.kurt.gym.gym.store.model.StoreSale;
import com.kurt.gym.helper.service.ApiMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreSaleRepository storeSaleRepository;

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

    @Override
    public void insertSale(Store store, BigDecimal value, Date date) {
        if (store == null) {
            // default store
            store = storeRepository.getReferenceById(1L);
        }

        BigDecimal storeCurrentValue = store.getTotalSales();
        storeCurrentValue = storeCurrentValue.add(value);

        StoreSale storeSale = storeSaleRepository.findStoreSaleByCreatedAt(date);

        if (storeSale == null) {
            storeSale = StoreSale.builder()
                    .sales(value)
                    .store(store)
                    .build();
        } else {
            BigDecimal newTotalSaleWithinDay = storeSale.getSales();
            newTotalSaleWithinDay = newTotalSaleWithinDay.add(value);
            storeSale.setSales(newTotalSaleWithinDay);
        }

        store.setTotalSales(storeCurrentValue);

        storeSaleRepository.saveAndFlush(storeSale);
        storeRepository.saveAndFlush(store);
    }

}
