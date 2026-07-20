package tn.anasazx.tunirate.productSuggestion.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.anasazx.tunirate.enums.SuggestionStatus;
import tn.anasazx.tunirate.productSuggestion.dto.ProductSuggestionRequest;
import tn.anasazx.tunirate.productSuggestion.dto.ProductSuggestionResponse;
import tn.anasazx.tunirate.productSuggestion.entity.ProductSuggestion;
import tn.anasazx.tunirate.productSuggestion.mapper.ProductSuggestionMapper;
import tn.anasazx.tunirate.productSuggestion.repository.ProductSuggestionRepository;
import tn.anasazx.tunirate.productSuggestion.service.ProductSuggestionService;
import tn.anasazx.tunirate.security.SecurityUtils;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSuggestionServiceImpl implements ProductSuggestionService {

    private final ProductSuggestionRepository productSuggestionRepository;
    private final UserRepository userRepository;
    private final ProductSuggestionMapper productSuggestionMapper;

    @Override
    public ProductSuggestionResponse createSuggestion(ProductSuggestionRequest request) {

        User user = getCurrentUser();

        ProductSuggestion suggestion = ProductSuggestion.builder()
                .name(request.name())
                .companyName(request.companyName())
                .description(request.description())
                .user(user)
                .build();

        return productSuggestionMapper.toResponse(productSuggestionRepository.save(suggestion));

    }

    @Override
    public List<ProductSuggestionResponse> getMySuggestions() {

        User user = getCurrentUser();

        return productSuggestionRepository.findAllByUserId(user.getId())
                .stream()
                .map(productSuggestionMapper::toResponse)
                .toList();

    }

    @Override
    public List<ProductSuggestionResponse> getAllSuggestions() {

        return productSuggestionRepository.findAll()
                .stream()
                .map(productSuggestionMapper::toResponse)
                .toList();

    }

    @Override
    public ProductSuggestionResponse updateStatus(Long id, SuggestionStatus status) {

        ProductSuggestion suggestion = productSuggestionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Suggestion not found"));

        suggestion.setStatus(status);

        return productSuggestionMapper.toResponse(productSuggestionRepository.save(suggestion));

    }

    private User getCurrentUser(){

        Long currentUserId = SecurityUtils.getCurrentUserId();

        return userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}