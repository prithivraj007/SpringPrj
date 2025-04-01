package com.pricingmicroservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "article")
@Data
public class ArticleEntity {
    @Id
    @Column(name = "article_id", unique = true, nullable = false)
    private String articleId;

    private String uom;
    private String description;
    private String brand;
    private String model;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PriceEntity> prices;
}
