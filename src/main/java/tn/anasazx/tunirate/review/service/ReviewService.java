package tn.anasazx.tunirate.review.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import tn.anasazx.tunirate.review.dto.MinimizedReviewResponse;
import tn.anasazx.tunirate.review.dto.ProductReviewsResponse;
import tn.anasazx.tunirate.review.dto.ReviewRequest;
import tn.anasazx.tunirate.review.dto.ReviewResponse;

import java.util.List;

public interface ReviewService {

    Page<ReviewResponse> getAllReviews(Pageable pageable);

    ReviewResponse getReviewById(Long id);

    ProductReviewsResponse getReviewsByProductId(Long productId, Pageable pageable, Long currentUserId);

    Page<MinimizedReviewResponse> getMyReviews(Pageable pageable, Long currentUserId);

    Page<ReviewResponse> getReviewsByUserId(Long userId, Pageable pageable);

    List<ReviewResponse> getMyCompanyReviews(Long currentUserId);

    @Transactional
    ReviewResponse createReview(ReviewRequest request, Long currentUserId);

    @Transactional
    ReviewResponse updateReview(Long id, ReviewRequest request, Long currentUserId);

    @Transactional
    void deleteReview(Long id, Long currentUserId);
}

