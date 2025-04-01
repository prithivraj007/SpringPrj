package com.pricingmicroservice.service;

import com.pricingmicroservice.dto.*;
import com.pricingmicroservice.entity.*;
import com.pricingmicroservice.exception.ResourceNotFoundException;
import com.pricingmicroservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private StoreRepository storeRepository;

    public PricingResponseDTO getPrices(String storeId, String articleId, int page, int pageSize) {
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + storeId));

        ArticleEntity article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with ID: " + articleId));

        List<PriceEntity> prices = priceRepository.findByStoreAndArticle(store, article);

        if (prices.isEmpty()) {
            throw new ResourceNotFoundException("No prices found for the given store and article.");
        }

        // Process prices (mark overlapped & merge)
        List<PriceEntity> processedPrices = processPrices(prices);

        // Pagination
        int fromIndex = Math.min((page - 1) * pageSize, processedPrices.size());
        int toIndex = Math.min(fromIndex + pageSize, processedPrices.size());
        List<PriceEntity> paginatedPrices = processedPrices.subList(fromIndex, toIndex);

        return PricingResponseDTO.builder()
                .generatedDate(ZonedDateTime.now())
                .article(article.getArticleId())
                .store(store.getStoreId())
                .meta(MetaDTO.builder().page(page).size(pageSize).build())
                .properties(PropertiesDTO.builder()
                        .uom(article.getUom())
                        .description(article.getDescription())
                        .brand(article.getBrand())
                        .model(article.getModel())
                        .build())
                .prices(paginatedPrices.stream().map(this::convertToPriceDTO).collect(Collectors.toList()))
                .build();
    }

    public List<PriceEntity> processPrices(List<PriceEntity> prices) {
        List<PriceEntity> result = new ArrayList<>();
        prices.sort(Comparator.comparing(PriceEntity::getValidFrom)); // Sort by validFrom

        for (PriceEntity current : prices) {
            boolean merged = false;
            for (PriceEntity existing : result) {
                if (isOverlapping(existing, current)) {
                    if (existing.getAmount().equals(current.getAmount())) {
                        // Merge logic
                        existing.setValidFrom(existing.getValidFrom().isBefore(current.getValidFrom()) ? existing.getValidFrom() : current.getValidFrom());
                        existing.setValidTo(existing.getValidTo().isAfter(current.getValidTo()) ? existing.getValidTo() : current.getValidTo());
                        merged = true;
                    } else {
                        // Overlap with different price, mark as overlapped
                        existing.setOverlapped(true);
                        current.setOverlapped(true);
                    }
                    break;
                }
            }
            if (!merged && !result.contains(current)) {
                result.add(current);
            }
        }
        return result;
    }

    private boolean isOverlapping(PriceEntity p1, PriceEntity p2) {
        return !p1.getValidTo().isBefore(p2.getValidFrom()) && !p2.getValidTo().isBefore(p1.getValidFrom());
    }

    private PriceDTO convertToPriceDTO(PriceEntity price) {
        return PriceDTO.builder()
                .type(price.getType())
                .subtype(price.getSubtype())
                .currency(price.getCurrency())
                .amount(price.getAmount())
                .validFrom(price.getValidFrom())
                .validTo(price.getValidTo())
                .build();
    }
}
