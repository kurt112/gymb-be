package com.kurt.gym.core.rest.api.util;

import com.kurt.gym.core.persistence.entity.Store;
import com.kurt.gym.core.persistence.entity.StoreSale;
import com.kurt.gym.core.persistence.repository.StoreRepository;
import com.kurt.gym.core.persistence.repository.StoreSaleRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Component
public class StoreUtil {
    private static StoreRepository storeRepository;
    private static StoreSaleRepository storeSaleRepository;

    StoreUtil() {
    }

    public static void initRepositories(StoreRepository storeRepository, StoreSaleRepository storeSaleRepository) {
        StoreUtil.storeRepository = storeRepository;
        StoreUtil.storeSaleRepository = storeSaleRepository;
    }

    @CachePut(value = "store", key = "store")
    public static Store save(Store store){
        storeRepository.saveAndFlush(store);

        return store;
    }
    @Cacheable(value = "store", key = "#id")
    public static Store findById(Long id){

        return storeRepository.findById(id).orElse(null);
    }

    @Caching(evict = {
            @CacheEvict(value = "store", key = "#id"),
            @CacheEvict(value = "store-reference", key = "#id"),
    })
    public static void deleteById(Long id){
        storeRepository.deleteById(id);
    }
    @Caching(evict = {
            @CacheEvict(value = "store", key = "#store.id"),
            @CacheEvict(value = "store-reference", key = "#store.id")
    })
    public static void delete(Store store){
        storeRepository.delete(store);
    }

    @Cacheable(value = "store-reference", key = "#id")
    public static Store getReferenceById(Long id) {
        return storeRepository.getReferenceById(id);
    }

    public static List<Store> getAllStore(){
        return storeRepository.findAll();
    }

    @Cacheable(value = "default_store")
    public static Store getDefaultStore(){
        return storeRepository.getReferenceById(1L);
    }

    public static void insertSale(Store store, BigDecimal value, Date date) {
        if (store == null) {
            // default store
            store = StoreUtil.getReferenceById(1L);
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
        StoreUtil.save(store);
    }


}
