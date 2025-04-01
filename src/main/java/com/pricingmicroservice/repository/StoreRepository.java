package com.pricingmicroservice.repository;

import com.pricingmicroservice.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<StoreEntity,String> {
}
