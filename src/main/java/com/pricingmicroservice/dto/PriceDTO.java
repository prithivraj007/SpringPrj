package com.pricingmicroservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class PriceDTO {
    private String type;
    private String subtype;
    private String currency;
    private Double amount;
    private ZonedDateTime validFrom;
    private ZonedDateTime validTo;
}
