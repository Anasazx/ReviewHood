package tn.anasazx.tunirate.category.mapper;


import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.category.dto.CategoryRequest;
import tn.anasazx.tunirate.category.dto.CategoryResponse;
import tn.anasazx.tunirate.category.entity.Category;

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
