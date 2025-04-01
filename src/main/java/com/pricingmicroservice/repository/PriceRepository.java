package com.pricingmicroservice.repository;

import com.pricingmicroservice.entity.ArticleEntity;
import com.pricingmicroservice.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pricingmicroservice.entity.PriceEntity;
import java.util.List;


public interface PriceRepository extends JpaRepository<PriceEntity, Long> {

    List<PriceEntity> findByStoreAndArticle(StoreEntity store, ArticleEntity article);
}
