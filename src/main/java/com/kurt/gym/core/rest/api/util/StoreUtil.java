package com.kurt.gym.core.rest.api.util;

import com.kurt.gym.core.persistence.entity.Store;
import com.kurt.gym.core.persistence.entity.StoreSale;
import com.kurt.gym.core.persistence.repository.StoreRepository;
import com.kurt.gym.core.persistence.repository.StoreSaleRepository;
import com.kurt.gym.helper.logger.LoggerUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Component
@Service
@RequiredArgsConstructor
public class StoreUtil {
    private static StoreRepository storeRepository;
    private static StoreSaleRepository storeSaleRepository;
    private static final Logger logger = LoggerFactory.getLogger(StoreUtil.class);

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

    @Caching(evict = {
            @CacheEvict(value = "store-sales", allEntries = true)
    })
    public static void insertSale(Store store, BigDecimal value, Date date) {
        LoggerUtil.printInfoWithDash(logger, "Inserting Store Sale");

        if (store == null) {
            // default store
            store = StoreUtil.getReferenceById(1L);
        }

        logger.info("The value that will insert in this sale: " + value.doubleValue());

        BigDecimal storeCurrentValue = store.getTotalSales();
        logger.info("Current StoreSale: " + storeCurrentValue.doubleValue());

        storeCurrentValue = storeCurrentValue.add(value);
        logger.info("The total after the sum: " +storeCurrentValue.doubleValue());

        BigDecimal storeCurrentVatPercentageThisDay = store.getVatPercentage().divide(new BigDecimal(100),6, RoundingMode.DOWN);
        logger.info("The percentage of vat: " + storeCurrentVatPercentageThisDay.doubleValue());

        BigDecimal vatCollectedInThisSale = storeCurrentVatPercentageThisDay.multiply(value);
        logger.info("The vat collected in this sale: " + vatCollectedInThisSale);


        StoreSale storeSale = storeSaleRepository.findStoreSaleByCreatedAt(date);

        if (storeSale == null) {
            storeSale = StoreSale.builder()
                    .sales(value)
                    .store(store)
                    .totalVatCollected(new BigDecimal(0))
                    .build();
        } else {
            BigDecimal newTotalSaleWithinDay = storeSale.getSales();
            newTotalSaleWithinDay = newTotalSaleWithinDay.add(value);
            storeSale.setSales(newTotalSaleWithinDay);
        }

        BigDecimal totalVatCollected = storeSale.getTotalVatCollected().add(vatCollectedInThisSale);

        BigDecimal newTotalVatInStore = store.getTotalVat().add(vatCollectedInThisSale);

        logger.info("The vat this day: " + totalVatCollected);
        store.setTotalSales(storeCurrentValue);
        store.setTotalVat(newTotalVatInStore);

        storeSale.setTotalVatCollected(totalVatCollected);
        storeSaleRepository.saveAndFlush(storeSale);
        StoreUtil.save(store);

        LoggerUtil.printInfoWithDash(logger, "Done Updating the sale");
    }

    public static Double findSalesInStoreBetweenDate(Long storeId, Date targetDate){
        LoggerUtil.printInfoWithDash(logger, "Querying Sales");
        logger.info("The Target Date: " + targetDate.toString());
        return storeSaleRepository.findSalesInTargetDate(storeId, targetDate);
    }

    public static Double findVatInStoreBetweenDate(Long storeId, Date targetDate){
        LoggerUtil.printInfoWithDash(logger, "Querying Vat");
        logger.info("The Target Date: " + targetDate.toString());
        return storeSaleRepository.findVatInTargetDate(storeId, targetDate);
    }


}
