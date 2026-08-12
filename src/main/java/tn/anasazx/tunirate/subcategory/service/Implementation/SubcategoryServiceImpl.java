package tn.anasazx.tunirate.subcategory.service.Implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.category.entity.Category;
import tn.anasazx.tunirate.category.repository.CategoryRepository;
import tn.anasazx.tunirate.subcategory.dto.SubcategoryRequest;
import tn.anasazx.tunirate.subcategory.dto.SubcategoryResponse;
import tn.anasazx.tunirate.subcategory.entity.Subcategory;
import tn.anasazx.tunirate.subcategory.repository.SubcategoryRepository;
import tn.anasazx.tunirate.subcategory.service.SubcategoryService;
import tn.anasazx.tunirate.subcategory.mapper.SubcategoryMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubcategoryServiceImpl implements SubcategoryService {
    private final SubcategoryRepository subcategoryRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryMapper subcategoryMapper;
    @Override
    public List<SubcategoryResponse> getAll() {
        return subcategoryRepository.findByCategoryName("Beauty")
                .stream()
                .map(subcategoryMapper::toResponse)
                .toList();
    }
    @Override
    public SubcategoryResponse getById(Long id) {
        return subcategoryMapper.toResponse(findSubcategory(id));
    }
    @Override
    public SubcategoryResponse create(SubcategoryRequest request) {
        Category category = findCategory(request.categoryId());
        Subcategory subcategory = subcategoryMapper.toEntity(request, category);
        return subcategoryMapper.toResponse(subcategoryRepository.save(subcategory));
    }
    @Override
    public SubcategoryResponse update(Long id, SubcategoryRequest request) {
        Subcategory subcategory = findSubcategory(id);
        Category category = findCategory(request.categoryId());
        subcategoryMapper.updateEntity(subcategory, request, category);
        return subcategoryMapper.toResponse(subcategoryRepository.save(subcategory));
    }
    @Override
    public void delete(Long id) {
        Subcategory subcategory = findSubcategory(id);
        subcategoryRepository.delete(subcategory);
    }
    @Override
    public Subcategory findSubcategory(Long id) {
        return subcategoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Subcategory not found"));
    }
    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }
}