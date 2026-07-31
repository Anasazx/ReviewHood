package tn.anasazx.tunirate.like.reviewLike.service.implementation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import tn.anasazx.tunirate.like.reviewLike.entity.ReviewLike;
import tn.anasazx.tunirate.like.reviewLike.repository.ReviewLikeRepository;
import tn.anasazx.tunirate.like.reviewLike.service.ReviewLikeService;
import tn.anasazx.tunirate.review.repository.ReviewRepository;
import tn.anasazx.tunirate.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ReviewLikeServiceImpl implements ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void addLike(Long reviewId, Long userId) {

        if (reviewLikeRepository.existsByReview_IdAndUser_Id(reviewId, userId)) {
            return; // already liked, no-op
        }

        ReviewLike like = ReviewLike.builder()
                .review(reviewRepository.getReferenceById(reviewId))
                .user(userRepository.getReferenceById(userId))
                .build();

        try {
            reviewLikeRepository.save(like);
            reviewRepository.incrementLikeCount(reviewId);
        } catch (DataIntegrityViolationException e) {
            // unique constraint hit = a concurrent request already liked this,
            // safe to swallow since the other request already incremented the count
        }
    }

    @Transactional
    @Override
    public void removeLike(Long reviewId, Long userId) {

        reviewLikeRepository
                .findByReview_IdAndUser_Id(reviewId, userId)
                .ifPresent(like -> {
                    reviewLikeRepository.delete(like);
                    reviewRepository.decrementLikeCount(reviewId);
                });
    }

    @Override
    public boolean isLikedByUser(Long reviewId, Long userId) {
        return reviewLikeRepository.existsByReview_IdAndUser_Id(reviewId, userId);
    }

    @Override
    public long countLikes(Long reviewId) {
        return reviewRepository.findLikeCountById(reviewId)
                .orElse(0L);
    }
}