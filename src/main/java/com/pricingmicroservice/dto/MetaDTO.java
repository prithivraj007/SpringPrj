package com.pricingmicroservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetaDTO {
    private int page;
    private int size;
}
