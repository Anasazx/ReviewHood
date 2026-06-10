package tn.anasazx.tunirate.categories.category.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.categories.category.dto.CategoryRequest;
import tn.anasazx.tunirate.categories.category.dto.CategoryResponse;
import tn.anasazx.tunirate.categories.category.entity.Category;
import tn.anasazx.tunirate.categories.category.mapper.CategoryMapper;
import tn.anasazx.tunirate.categories.category.repository.CategoryRepository;
import tn.anasazx.tunirate.categories.category.service.CategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAllByOrderByNameAsc()
                .stream()
                .map(categoryMapper::toResponse)
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
        categoryRepository.delete(category);
    }

    @Override
    public Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }
}