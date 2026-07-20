package tn.anasazx.tunirate.productSuggestion.service;

import tn.anasazx.tunirate.enums.SuggestionStatus;
import tn.anasazx.tunirate.productSuggestion.dto.ProductSuggestionRequest;
import tn.anasazx.tunirate.productSuggestion.dto.ProductSuggestionResponse;

import java.util.List;

public interface ProductSuggestionService {
    ProductSuggestionResponse createSuggestion(ProductSuggestionRequest request);
    List<ProductSuggestionResponse> getMySuggestions();
    List<ProductSuggestionResponse> getAllSuggestions();
    ProductSuggestionResponse updateStatus(Long id, SuggestionStatus status);
}
