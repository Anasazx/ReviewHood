package tn.anasazx.tunirate.subcategory.service;

import tn.anasazx.tunirate.subcategory.dto.AdminSubcategoryResponse;
import tn.anasazx.tunirate.subcategory.dto.SubcategoryRequest;
import tn.anasazx.tunirate.subcategory.dto.SubcategoryResponse;
import tn.anasazx.tunirate.subcategory.entity.Subcategory;

import java.util.List;

public interface SubcategoryService {
    List<SubcategoryResponse> getAll();

    // Every subcategory of every category, with how many products use it
    List<AdminSubcategoryResponse> getAllAsAdmin();
    SubcategoryResponse getById(Long id);
    SubcategoryResponse create(SubcategoryRequest request);
    SubcategoryResponse update(Long id, SubcategoryRequest request);
    void delete(Long id);
    Subcategory findSubcategory(Long id);
}
