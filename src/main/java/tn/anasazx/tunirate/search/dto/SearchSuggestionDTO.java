package tn.anasazx.tunirate.search.dto;

import tn.anasazx.tunirate.enums.SearchSuggestionType;

public record SearchSuggestionDTO(
        Long id,
        String name,
        SearchSuggestionType type,
        String imageUrl,
        String secondaryText
) {}

