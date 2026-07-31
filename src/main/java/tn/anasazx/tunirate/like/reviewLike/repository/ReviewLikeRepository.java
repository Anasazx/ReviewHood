package tn.anasazx.tunirate.like.reviewLike.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.anasazx.tunirate.like.reviewLike.entity.ReviewLike;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    boolean existsByReview_IdAndUser_Id(Long reviewId, Long userId);
    long countByReview_Id(Long reviewId);
    Optional<ReviewLike> findByReview_IdAndUser_Id(Long reviewId, Long userId);
    void deleteByReview_IdAndUser_Id(Long reviewId, Long userId);
}
