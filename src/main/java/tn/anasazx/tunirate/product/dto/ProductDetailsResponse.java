package tn.anasazx.tunirate.product.dto;
import tn.anasazx.tunirate.review.dto.ReviewResponse;

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
        List<ReviewResponse> reviews,
        List<ProductImageResponse> images

) {}
