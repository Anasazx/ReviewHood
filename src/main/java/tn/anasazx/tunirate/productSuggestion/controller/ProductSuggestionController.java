package tn.anasazx.tunirate.productSuggestion.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.anasazx.tunirate.productSuggestion.dto.ProductSuggestionRequest;
import tn.anasazx.tunirate.productSuggestion.dto.ProductSuggestionResponse;
import tn.anasazx.tunirate.productSuggestion.dto.UpdateStatusRequest;
import tn.anasazx.tunirate.productSuggestion.service.ProductSuggestionService;

import java.util.List;

@RestController
@RequestMapping("/suggestions")
@RequiredArgsConstructor
public class ProductSuggestionController {

    private final ProductSuggestionService productSuggestionService;

    @PostMapping
    public ResponseEntity<ProductSuggestionResponse> createSuggestion(@RequestBody ProductSuggestionRequest request) {
        return ResponseEntity.ok(productSuggestionService.createSuggestion(request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ProductSuggestionResponse>> getMySuggestions() {
        return ResponseEntity.ok(productSuggestionService.getMySuggestions());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ProductSuggestionResponse>> getAllSuggestions() {
        return ResponseEntity.ok(productSuggestionService.getAllSuggestions());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ProductSuggestionResponse> updateStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(productSuggestionService.updateStatus(id, request.status()));
    }

}