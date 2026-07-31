package tn.anasazx.tunirate.subcategory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.anasazx.tunirate.subcategory.dto.SubcategoryRequest;
import tn.anasazx.tunirate.subcategory.dto.SubcategoryResponse;
import tn.anasazx.tunirate.subcategory.service.SubcategoryService;

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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<SubcategoryResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(subcategoryService.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SubcategoryResponse> create(@Valid @RequestBody SubcategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subcategoryService.create(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<SubcategoryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SubcategoryRequest request
    ) {
        return ResponseEntity.ok(subcategoryService.update(id, request));
    }

    //TODO: Remove the delete and make it soft delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subcategoryService.delete(id);
        return ResponseEntity.noContent().build();

    }
}