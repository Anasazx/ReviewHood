package tn.anasazx.tunirate.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.anasazx.tunirate.review.dto.ReviewRequest;
import tn.anasazx.tunirate.review.dto.ReviewResponse;
import tn.anasazx.tunirate.review.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	@GetMapping
	public ResponseEntity<List<ReviewResponse>> getAllReviews() {
		return ResponseEntity.ok(reviewService.getAllReviews());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ReviewResponse> getReviewById(@PathVariable Long id) {
		return ResponseEntity.ok(reviewService.getReviewById(id));
	}

	@GetMapping("/by-product/{productId}")
	public ResponseEntity<List<ReviewResponse>> getReviewsByProductId(@PathVariable Long productId) {
		return ResponseEntity.ok(reviewService.getReviewsByProductId(productId));
	}

	@GetMapping("/by-user/{userId}")
	public ResponseEntity<List<ReviewResponse>> getReviewsByUserId(@PathVariable Long userId) {
		return ResponseEntity.ok(reviewService.getReviewsByUserId(userId));
	}

	@PostMapping
	public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ReviewResponse> updateReview(
			@PathVariable Long id,
			@Valid @RequestBody ReviewRequest request
	) {
		return ResponseEntity.ok(reviewService.updateReview(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
		reviewService.deleteReview(id);
		return ResponseEntity.noContent().build();
	}
}
