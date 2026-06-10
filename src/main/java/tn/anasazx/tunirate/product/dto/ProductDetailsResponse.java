package tn.anasazx.tunirate.product.dto;

import java.util.List;

public record ProductDetailsResponse(
        Long id,
        String name,
        String description,
        String category,
        String subcategory,
        Long companyId,
        String companyName,
        boolean companyIsVerified,
        String companyLogoUrl,
        double averageRating,
        long reviewsCount,
        List<ProductImageResponse> images

) {}
