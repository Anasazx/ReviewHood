package tn.anasazx.tunirate.categories.category.service;

import tn.anasazx.tunirate.categories.category.dto.CategoryRequest;
import tn.anasazx.tunirate.categories.category.dto.CategoryResponse;
import tn.anasazx.tunirate.categories.category.entity.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryById(Long id);
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);
    Category findCategory(Long id);
}
