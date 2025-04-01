package com.pricingmicroservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "store")
@Data
public class StoreEntity {

    @Id
    @Column(name = "store_id", unique = true, nullable = false)
    private String storeId;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PriceEntity> prices;
}
