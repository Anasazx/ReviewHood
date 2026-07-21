package tn.anasazx.tunirate.productSuggestion.mapper;

import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.productSuggestion.dto.ProductSuggestionResponse;
import tn.anasazx.tunirate.productSuggestion.entity.ProductSuggestion;

@Component
public class ProductSuggestionMapper {

    public ProductSuggestionResponse toResponse(ProductSuggestion suggestion) {

        return new ProductSuggestionResponse(
                suggestion.getId(),
                suggestion.getName(),
                suggestion.getCompanyName(),
                suggestion.getDescription(),
                suggestion.getStatus() != null ? suggestion.getStatus().name() : null,
                suggestion.getCreatedAt(),
                suggestion.getProduct() == null ? null : suggestion.getProduct().getId(),
                suggestion.getProduct() == null ? null : suggestion.getProduct().getName()
        );

    }

}
