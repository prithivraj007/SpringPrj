package com.pricingmicroservice.repository;

import com.pricingmicroservice.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<ArticleEntity,String> {
}
