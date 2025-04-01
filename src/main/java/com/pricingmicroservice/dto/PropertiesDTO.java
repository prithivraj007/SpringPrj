package com.pricingmicroservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PropertiesDTO {
    private String uom;
    private String description;
    private String brand;
    private String model;
}
