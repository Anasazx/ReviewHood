package tn.anasazx.tunirate.review.dto;

import org.springframework.data.domain.Page;

public record ProductReviewsResponse(
        ReviewResponse myReview,
        Page<ReviewResponse> reviews
) {}