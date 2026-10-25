package tn.anasazx.tunirate.productSuggestion.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.enums.SuggestionStatus;
import tn.anasazx.tunirate.product.entity.Product;
import tn.anasazx.tunirate.product.repository.ProductRepository;
import tn.anasazx.tunirate.productSuggestion.dto.ProductSuggestionRequest;
import tn.anasazx.tunirate.productSuggestion.dto.ProductSuggestionResponse;
import tn.anasazx.tunirate.productSuggestion.dto.UpdateStatusRequest;
import tn.anasazx.tunirate.productSuggestion.entity.ProductSuggestion;
import tn.anasazx.tunirate.productSuggestion.mapper.ProductSuggestionMapper;
import tn.anasazx.tunirate.productSuggestion.repository.ProductSuggestionRepository;
import tn.anasazx.tunirate.productSuggestion.service.ProductSuggestionService;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSuggestionServiceImpl implements ProductSuggestionService {

    private final ProductSuggestionRepository productSuggestionRepository;
    private final UserRepository userRepository;
    private final ProductSuggestionMapper productSuggestionMapper;
    private final ProductRepository productRepository;

    @Override
    public ProductSuggestionResponse createSuggestion(ProductSuggestionRequest request, Long currentUserId) {

        User user = getCurrentUser(currentUserId);

        ProductSuggestion suggestion = ProductSuggestion.builder()
                .name(request.name())
                .companyName(request.companyName())
                .description(request.description())
                .user(user)
                .build();

        return productSuggestionMapper.toResponse(productSuggestionRepository.save(suggestion));

    }

    @Override
    public List<ProductSuggestionResponse> getMySuggestions(Long currentUserId) {

        User user = getCurrentUser(currentUserId);

        return productSuggestionRepository.findAllByUserId(user.getId())
                .stream()
                .map(productSuggestionMapper::toResponse)
                .toList();

    }

    @Override
    public List<ProductSuggestionResponse> getAllSuggestions(SuggestionStatus status) {

        List<ProductSuggestion> suggestions = status == null
                ? productSuggestionRepository.findAllByOrderByCreatedAtDesc()
                : productSuggestionRepository.findAllByStatusOrderByCreatedAtDesc(status);

        return suggestions
                .stream()
                .map(productSuggestionMapper::toResponse)
                .toList();

    }

    @Override
    public ProductSuggestionResponse updateStatus(Long id, UpdateStatusRequest request) {

        ProductSuggestion suggestion = productSuggestionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Suggestion not found"));

        if (request.status() == SuggestionStatus.APPROVED) {
            if (request.productId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pick the product that matches this suggestion");
            }

            Product product = productRepository.findById(request.productId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
            suggestion.setProduct(product);
        }

        suggestion.setStatus(request.status());

        return productSuggestionMapper.toResponse(productSuggestionRepository.save(suggestion));
    }

    private User getCurrentUser(Long currentUserId){
        return userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}