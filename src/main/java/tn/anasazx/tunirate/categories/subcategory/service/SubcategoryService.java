package tn.anasazx.tunirate.categories.subcategory.service;

import tn.anasazx.tunirate.categories.subcategory.dto.SubcategoryRequest;
import tn.anasazx.tunirate.categories.subcategory.dto.SubcategoryResponse;
import tn.anasazx.tunirate.categories.subcategory.entity.Subcategory;

import java.util.List;

public interface SubcategoryService {
    List<SubcategoryResponse> getAll();
    SubcategoryResponse getById(Long id);
    SubcategoryResponse create(SubcategoryRequest request);
    SubcategoryResponse update(Long id, SubcategoryRequest request);
    void delete(Long id);
    Subcategory findSubcategory(Long id);
}
