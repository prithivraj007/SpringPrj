package com.pricingmicroservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.ZonedDateTime;

@Entity
@Table(name = "price")
@Data
public class PriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String subtype;
    private String currency;
    private Double amount;
    private ZonedDateTime validFrom;
    private ZonedDateTime validTo;
    private boolean overlapped = false;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private ArticleEntity article;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;
}
