package tn.anasazx.tunirate.review.service;

import tn.anasazx.tunirate.review.dto.ReviewRequest;
import tn.anasazx.tunirate.review.dto.ReviewResponse;

import java.util.List;

public interface ReviewService {

    List<ReviewResponse> getAllReviews();

    ReviewResponse getReviewById(Long id);

    List<ReviewResponse> getReviewsByProductId(Long productId);

    List<ReviewResponse> getReviewsByUserId(Long userId);

    ReviewResponse createReview(ReviewRequest request);

    ReviewResponse updateReview(Long id, ReviewRequest request);

    void deleteReview(Long id);
}

