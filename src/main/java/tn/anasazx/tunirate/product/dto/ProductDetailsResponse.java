package tn.anasazx.tunirate.product.dto;

import java.util.List;

public record ProductDetailsResponse(
        Long id,
        String name,
        String description,
        String category,
        String subcategory,
        String companyName,
        double averageRating,
        long reviewsCount,
        List<ProductImageResponse> images

) {}
