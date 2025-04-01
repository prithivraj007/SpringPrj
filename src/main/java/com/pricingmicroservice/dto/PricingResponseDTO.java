package com.pricingmicroservice.dto;

import lombok.Builder;
import lombok.Data;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
public class PricingResponseDTO {
    private ZonedDateTime generatedDate;
    private String article;
    private String store;
    private MetaDTO meta;
    private PropertiesDTO properties;
    private List<PriceDTO> prices;
}
