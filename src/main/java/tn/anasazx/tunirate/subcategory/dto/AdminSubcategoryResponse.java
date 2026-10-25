package tn.anasazx.tunirate.subcategory.dto;

public record AdminSubcategoryResponse(
        Long id,
        String name,
        Long categoryId,
        String categoryName,
        long productCount
) {}
