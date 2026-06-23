package tn.anasazx.tunirate.category.service;

import tn.anasazx.tunirate.category.dto.CategoryRequest;
import tn.anasazx.tunirate.category.dto.CategoryResponse;
import tn.anasazx.tunirate.category.entity.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryById(Long id);
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);
    Category findCategory(Long id);
}
