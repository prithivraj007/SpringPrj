package com.pricingmicroservice.controller;

import com.pricingmicroservice.dto.ErrorResponse;
import com.pricingmicroservice.dto.PricingResponseDTO;
import com.pricingmicroservice.exception.ResourceNotFoundException;
import com.pricingmicroservice.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/pricing/v1")
public class PriceController {

    @Autowired
    private PriceService priceService;

    @GetMapping("/prices/{storeId}/{articleId}")
    public ResponseEntity<?> getPrices(
            @PathVariable String storeId,
            @PathVariable String articleId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int pageSize) {

        try {
            PricingResponseDTO response = priceService.getPrices(storeId, articleId, page, pageSize);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "Not_Found",
                    "Unavailable prices",
                    404,
                    ex.getMessage(),
                    LocalDateTime.now()
            );
            return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
        }
    }
}
