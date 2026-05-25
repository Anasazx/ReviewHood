package tn.anasazx.tunirate.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import tn.anasazx.tunirate.review.dto.ReviewRequest;
import tn.anasazx.tunirate.review.dto.ReviewResponse;

import java.util.List;

public interface ReviewService {

    List<ReviewResponse> getAllReviews();

    ReviewResponse getReviewById(Long id);

    ReviewResponse createReview(ReviewRequest request);

    ReviewResponse updateReview(Long id, ReviewRequest request);

    void deleteReview(Long id);

    Page<ReviewResponse> getReviewsByUserId(Long userId, Pageable pageable);

    Page<ReviewResponse> getReviewsByProductId(Long productId, Pageable pageable);

}

