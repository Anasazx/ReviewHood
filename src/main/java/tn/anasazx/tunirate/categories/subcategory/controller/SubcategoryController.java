package tn.anasazx.tunirate.categories.subcategory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.anasazx.tunirate.categories.subcategory.dto.SubcategoryRequest;
import tn.anasazx.tunirate.categories.subcategory.dto.SubcategoryResponse;
import tn.anasazx.tunirate.categories.subcategory.service.SubcategoryService;

import java.util.List;

@RestController
@RequestMapping("/subcategories")
@RequiredArgsConstructor
public class SubcategoryController {

    private final SubcategoryService subcategoryService;

    @GetMapping
    public ResponseEntity<List<SubcategoryResponse>> getAll() {
        return ResponseEntity.ok(subcategoryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubcategoryResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(subcategoryService.getById(id));
    }

    @PostMapping
    public ResponseEntity<SubcategoryResponse> create(@Valid @RequestBody SubcategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subcategoryService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubcategoryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SubcategoryRequest request
    ) {
        return ResponseEntity.ok(subcategoryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subcategoryService.delete(id);
        return ResponseEntity.noContent().build();

    }
}