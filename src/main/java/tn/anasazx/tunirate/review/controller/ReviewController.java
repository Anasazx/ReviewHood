package tn.anasazx.tunirate.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.anasazx.tunirate.review.dto.MinimizedReviewResponse;
import tn.anasazx.tunirate.review.dto.ProductReviewsResponse;
import tn.anasazx.tunirate.review.dto.ReviewRequest;
import tn.anasazx.tunirate.review.dto.ReviewResponse;
import tn.anasazx.tunirate.review.service.ReviewService;
import tn.anasazx.tunirate.security.SecurityUtils;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<Page<ReviewResponse>> getAllReviews(Pageable pageable) {
		return ResponseEntity.ok(reviewService.getAllReviews(pageable));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<ReviewResponse> getReviewById(@PathVariable Long id) {
		return ResponseEntity.ok(reviewService.getReviewById(id));
	}

	@GetMapping("/product/{productId}")
	public ResponseEntity<ProductReviewsResponse> getReviewsByProductId(@PathVariable Long productId, Pageable pageable) {
		Long currentUserId = SecurityUtils.getCurrentUserIdOrNull();
		return ResponseEntity.ok(reviewService.getReviewsByProductId(productId, pageable, currentUserId));
	}

	@GetMapping("/my")
	public ResponseEntity<Page<MinimizedReviewResponse>> getMyReviews(Pageable pageable) {
		Long currentUserId = SecurityUtils.getCurrentUserId();
		return ResponseEntity.ok(reviewService.getMyReviews(pageable, currentUserId));
	}

	//TODO: Implement pagination
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/c/my")
	public ResponseEntity<List<ReviewResponse>> getMyCompanyReviews() {
		Long currentUserId = SecurityUtils.getCurrentUserId();
		return ResponseEntity.ok(reviewService.getMyCompanyReviews(currentUserId));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/by-user/{userId}")
	public ResponseEntity<Page<ReviewResponse>> getReviewsByUserId(@PathVariable Long userId, Pageable pageable) {
		return ResponseEntity.ok(reviewService.getReviewsByUserId(userId, pageable));
	}


	@PostMapping
	public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewRequest request) {
		Long currentUserId = SecurityUtils.getCurrentUserId();
		return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(request, currentUserId));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ReviewResponse> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewRequest request) {
		Long currentUserId = SecurityUtils.getCurrentUserId();
		return ResponseEntity.ok(reviewService.updateReview(id, request, currentUserId));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
		Long currentUserId = SecurityUtils.getCurrentUserId();
		reviewService.deleteReview(id, currentUserId);
		return ResponseEntity.noContent().build();
	}

}
