package com.kurt.gym.core.persistence.repository;

import com.kurt.gym.core.persistence.entity.StoreSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface StoreSaleRepository extends JpaRepository<StoreSale, Long>{

    StoreSale findStoreSaleByCreatedAt(Date date);

    @Query("select t.sales from StoreSale t where t.store.id = ?1 and t.createdAt = ?2")
    Double findSalesInTargetDate(Long id, Date targetDate);

}
