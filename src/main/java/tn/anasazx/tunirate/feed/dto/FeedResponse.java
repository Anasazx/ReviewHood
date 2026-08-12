package tn.anasazx.tunirate.feed.dto;

import tn.anasazx.tunirate.product.dto.ProductResponse;
import tn.anasazx.tunirate.review.dto.MinimizedReviewResponse;
import tn.anasazx.tunirate.subcategory.dto.SubcategoryResponse;

import java.util.List;

public record FeedResponse(
        List<ProductResponse> products,
        List<MinimizedReviewResponse> reviews,
        List<SubcategoryResponse> categories
) {}