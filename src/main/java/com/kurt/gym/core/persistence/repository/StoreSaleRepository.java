package com.kurt.gym.core.persistence.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kurt.gym.core.persistence.entity.StoreSale;
import org.springframework.data.jpa.repository.Query;

public interface StoreSaleRepository extends JpaRepository<StoreSale, Long>{

    StoreSale findStoreSaleByCreatedAt(Date date);

    @Query("select t.sales from StoreSale t where t.store.id = ?1 and t.createdAt = ?2 ")
    Double findSalesInStoreWithDate(Long id, Date date);

}
