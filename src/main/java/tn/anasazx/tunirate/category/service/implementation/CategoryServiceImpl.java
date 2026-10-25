package tn.anasazx.tunirate.category.service.implementation;

import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import tn.anasazx.tunirate.subcategory.entity.Subcategory;
import tn.anasazx.tunirate.enums.ProductStatus;
import tn.anasazx.tunirate.category.dto.CategoryTreeResponse;
import tn.anasazx.tunirate.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.category.dto.CategoryRequest;
import tn.anasazx.tunirate.category.dto.CategoryResponse;
import tn.anasazx.tunirate.category.entity.Category;
import tn.anasazx.tunirate.category.mapper.CategoryMapper;
import tn.anasazx.tunirate.category.repository.CategoryRepository;
import tn.anasazx.tunirate.category.service.CategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductRepository productRepository;

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAllByOrderByNameAsc()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    // Categories with their subcategories (alphabetical) and published product counts, for public navigation
    @Override
    public List<CategoryTreeResponse> getCategoryTree() {
        Map<Long, Long> counts = new HashMap<>();
        for (Object[] row : productRepository.countBySubcategoryGroupedWithStatus(ProductStatus.PUBLISHED)) {
            counts.put((Long) row[0], (Long) row[1]);
        }

        return categoryRepository.findAllByOrderByNameAsc().stream()
                .map(category -> {
                    List<CategoryTreeResponse.Node> subs = category.getSubcategories().stream()
                            .sorted(Comparator.comparing(Subcategory::getName, String.CASE_INSENSITIVE_ORDER))
                            .map(s -> new CategoryTreeResponse.Node(s.getId(), s.getName(), counts.getOrDefault(s.getId(), 0L)))
                            .toList();
                    long total = subs.stream().mapToLong(CategoryTreeResponse.Node::productCount).sum();
                    return new CategoryTreeResponse(category.getId(), category.getName(), total, subs);
                })
                .toList();
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = findCategory(id);
        return categoryMapper.toResponse(category);
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        Category saved = categoryRepository.save(category);
        return categoryMapper.toResponse(saved);
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = findCategory(id);

        categoryMapper.updateEntity(category, request);

        Category saved = categoryRepository.save(category);
        return categoryMapper.toResponse(saved);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = findCategory(id);

        // Deleting a category also deletes its subcategories, which products may still use
        long products = productRepository.countBySubcategoryCategoryId(id);
        if (products > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    products + " products use this category's subcategories. Move them first.");
        }

        categoryRepository.delete(category);
    }

    @Override
    public Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }
}