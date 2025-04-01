package com.pricingmicroservice.service;

import com.pricingmicroservice.entity.PriceEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class PricingServiceTest {

    @Autowired
    private PriceService pricingService = new PriceService(); // Replace with actual instantiation

    @Test
    public void testNonOverlappingPrices() {
        PriceEntity price1 = createPrice("retail", "regular", 30.0, "2023-01-01T00:00:00Z", "2023-01-31T23:59:59Z");
        PriceEntity price2 = createPrice("retail", "discounted", 25.0, "2023-02-01T00:00:00Z", "2023-02-28T23:59:59Z");

        List<PriceEntity> result = pricingService.processPrices(Arrays.asList(price1, price2));

        assertEquals(2, result.size());
        assertFalse(result.get(0).isOverlapped());
        assertFalse(result.get(1).isOverlapped());
    }

    @Test
    public void testExactOverlappingPricesWithSameAmount() {
        PriceEntity price1 = createPrice("retail", "regular", 30.0, "2023-01-01T00:00:00Z", "2023-01-31T23:59:59Z");
        PriceEntity price2 = createPrice("retail", "regular", 30.0, "2023-01-15T00:00:00Z", "2023-02-15T23:59:59Z");

        List<PriceEntity> result = pricingService.processPrices(Arrays.asList(price1, price2));

        assertEquals(1, result.size());
        assertEquals("2023-01-01T00:00Z", result.get(0).getValidFrom().toString());
        assertEquals("2023-02-15T23:59:59Z", result.get(0).getValidTo().toString());
        assertFalse(result.get(0).isOverlapped());
    }

    @Test
    public void testOverlappingPricesWithDifferentAmounts() {
        PriceEntity price1 = createPrice("retail", "regular", 30.0, "2023-01-01T00:00:00Z", "2023-01-31T23:59:59Z");
        PriceEntity price2 = createPrice("retail", "discounted", 25.0, "2023-01-15T00:00:00Z", "2023-02-15T23:59:59Z");

        List<PriceEntity> result = pricingService.processPrices(Arrays.asList(price1, price2));

        assertEquals(2, result.size());
        assertTrue(result.get(0).isOverlapped());
        assertTrue(result.get(1).isOverlapped());
    }

    @Test
    public void testContinuousOverlappingPricesWithSameAmount() {
        PriceEntity price1 = createPrice("retail", "regular", 30.0, "2023-01-01T00:00:00Z", "2023-01-31T23:59:59Z");
        PriceEntity price2 = createPrice("retail", "regular", 30.0, "2023-02-01T00:00:00Z", "2023-02-28T23:59:59Z");

        List<PriceEntity> result = pricingService.processPrices(Arrays.asList(price1, price2));

        assertEquals(2, result.size());
        assertEquals("2023-01-01T00:00Z", result.get(0).getValidFrom().toString());
        assertEquals("2023-01-31T23:59:59Z", result.get(0).getValidTo().toString());
        assertFalse(result.get(0).isOverlapped());
    }

    @Test
    public void testAlexScenario() {
        PriceEntity price1 = createPrice("retail", "regular", 30.0, "2023-01-01T00:00:00Z", "2023-01-31T23:59:59Z");
        PriceEntity price2 = createPrice("retail", "regular", 29.0, "2023-01-15T00:00:00Z", "2023-02-15T23:59:59Z");
        PriceEntity price3 = createPrice("retail", "regular", 29.0, "2023-01-16T00:00:00Z", "2023-03-15T23:59:59Z");
        PriceEntity price4 = createPrice("retail", "regular", 29.0, "2023-01-14T00:00:00Z", "2023-03-17T23:59:59Z");
        PriceEntity price5 = createPrice("retail", "regular", 30.0, "2023-01-02T00:00:00Z", "2023-03-30T23:59:59Z");

        List<PriceEntity> result = pricingService.processPrices(Arrays.asList(price1, price2, price3, price4, price5));

        assertEquals(2, result.size());
        assertEquals("2023-01-01T00:00Z", result.get(0).getValidFrom().toString());
        assertEquals("2023-03-30T23:59:59Z", result.get(0).getValidTo().toString());
        assertTrue(result.get(0).isOverlapped());
        assertEquals("2023-01-14T00:00Z", result.get(1).getValidFrom().toString());
        assertEquals("2023-03-17T23:59:59Z", result.get(1).getValidTo().toString());
        assertTrue(result.get(1).isOverlapped());
    }

    // Helper method
    private PriceEntity createPrice(String type, String subtype, double amount, String validFrom, String validTo) {
        PriceEntity price = new PriceEntity();
        price.setType(type);
        price.setSubtype(subtype);
        price.setCurrency("CAD");
        price.setAmount(amount);
        price.setValidFrom(ZonedDateTime.parse(validFrom));
        price.setValidTo(ZonedDateTime.parse(validTo));
        return price;
    }
}

