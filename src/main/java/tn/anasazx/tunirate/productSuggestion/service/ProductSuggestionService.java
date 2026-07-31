package tn.anasazx.tunirate.productSuggestion.service;

import tn.anasazx.tunirate.productSuggestion.dto.ProductSuggestionRequest;
import tn.anasazx.tunirate.productSuggestion.dto.ProductSuggestionResponse;
import tn.anasazx.tunirate.productSuggestion.dto.UpdateStatusRequest;

import java.util.List;

public interface ProductSuggestionService {
    ProductSuggestionResponse createSuggestion(ProductSuggestionRequest request, Long currentUserId);
    List<ProductSuggestionResponse> getMySuggestions(Long currentUserId);
    List<ProductSuggestionResponse> getAllSuggestions();
    ProductSuggestionResponse updateStatus(Long id, UpdateStatusRequest request);
}
