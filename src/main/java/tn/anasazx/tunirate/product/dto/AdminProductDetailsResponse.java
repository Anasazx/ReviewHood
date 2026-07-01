package tn.anasazx.tunirate.product.dto;

import java.util.List;

public record AdminProductDetailsResponse(
        Long id,
        String name,
        String description,
        String category,
        Long subcategoryId,
        String subcategoryName,

        Long companyId,
        String companyName,
        boolean companyIsVerified,
        String companyLogoUrl,
        double averageRating,
        long reviewsCount,
        List<ProductImageResponse> images,
        String createdByName,
        Long createdById,
        String updatedByName,
        Long updatedById,
        String status
) {
}
