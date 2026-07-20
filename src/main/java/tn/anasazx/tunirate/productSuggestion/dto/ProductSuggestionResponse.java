package tn.anasazx.tunirate.productSuggestion.dto;

import java.time.LocalDateTime;

public record ProductSuggestionResponse(
        Long id,
        String name,
        String companyName,
        String description,
        String status,
        LocalDateTime createdAt
) {}