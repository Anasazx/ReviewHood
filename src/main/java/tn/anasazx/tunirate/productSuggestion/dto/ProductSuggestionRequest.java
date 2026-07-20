package tn.anasazx.tunirate.productSuggestion.dto;


public record ProductSuggestionRequest(
        String name,
        String companyName,
        String description
) {}