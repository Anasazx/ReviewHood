package tn.anasazx.tunirate.categories.category.mapper;


import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.categories.category.dto.CategoryRequest;
import tn.anasazx.tunirate.categories.category.dto.CategoryResponse;
import tn.anasazx.tunirate.categories.category.entity.Category;

@Component
public class CategoryMapper {
    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }
    public Category toEntity(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());
        return category;
    }
    public void updateEntity(Category category, CategoryRequest request) {
        category.setName(request.name());
    }
}
