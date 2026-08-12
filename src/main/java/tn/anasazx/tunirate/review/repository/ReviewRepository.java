package tn.anasazx.tunirate.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.anasazx.tunirate.review.entity.Review;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByProductId(Long productId, Pageable pageable);

    List<Review> findAllByProductId(Long productId);

    Page<Review> findByUserId(Long userId, Pageable pageable);
    Optional<Review> findByUserIdAndProductId(Long userId, Long productId);


    Long countByProductCompanyId(Long companyId);

    List<Review> findByProductCompanyId(Long companyId);

    @Query("""
    SELECT AVG(r.rating)
    FROM Review r
    WHERE r.product.company.id = :companyId
    """)
    Double findAverageRatingByCompanyId(Long companyId);

    List<Review> findTop3ByProductCompanyIdOrderByCreatedAtDesc(Long productCompanyId);


    Page<Review> findReviewsByUserId(Long userId, Pageable pageable);

    @Modifying
    @Query("UPDATE Review r SET r.likeCount = r.likeCount + 1 WHERE r.id = :reviewId")
    void incrementLikeCount(@Param("reviewId") Long reviewId);

    @Modifying
    @Query("UPDATE Review r SET r.likeCount = r.likeCount - 1 WHERE r.id = :reviewId AND r.likeCount > 0")
    void decrementLikeCount(@Param("reviewId") Long reviewId);

    @Query("SELECT r.likeCount FROM Review r WHERE r.id = :reviewId")
    Optional<Long> findLikeCountById(@Param("reviewId") Long reviewId);


    @Modifying
    @Query(value = """
    UPDATE reviews r
    SET like_count = (SELECT COUNT(*) FROM review_likes rl WHERE rl.review_id = r.id)
    WHERE r.like_count != (SELECT COUNT(*) FROM review_likes rl WHERE rl.review_id = r.id)
    """, nativeQuery = true)
    int reconcileAllLikeCounts();



    @Query(value = """
    SELECT CAST(created_at AS date) AS day, COUNT(*) AS count
    FROM reviews
    WHERE created_at >= :startDate
    GROUP BY CAST(created_at AS date)
    ORDER BY day
    """, nativeQuery = true)
    List<Object[]> countReviewsByDayRaw(@Param("startDate") LocalDateTime startDate);

    List<Review> findByContentIsNotNullOrderByCreatedAtDesc(Pageable pageable);


}