package tn.anasazx.tunirate.categories.subcategory.subcategoryMapper;

import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.categories.category.entity.Category;
import tn.anasazx.tunirate.categories.subcategory.dto.SubcategoryRequest;
import tn.anasazx.tunirate.categories.subcategory.dto.SubcategoryResponse;
import tn.anasazx.tunirate.categories.subcategory.entity.Subcategory;

@Component
public class SubcategoryMapper {
    public SubcategoryResponse toResponse(Subcategory subcategory) {
        return new SubcategoryResponse(
                subcategory.getId(),
                subcategory.getName(),
                subcategory.getCategory().getId(),
                subcategory.getCategory().getName()
        );
    }
    public Subcategory toEntity(SubcategoryRequest request, Category category) {
        Subcategory subcategory = new Subcategory();
        subcategory.setName(request.name());
        subcategory.setCategory(category);
        return subcategory;
    }
    public void updateEntity(Subcategory subcategory, SubcategoryRequest request, Category category) {
        subcategory.setName(request.name());
        subcategory.setCategory(category);
    }
}