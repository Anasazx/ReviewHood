package tn.anasazx.tunirate.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.anasazx.tunirate.review.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByProductId(Long productId, Pageable pageable);
    Page<Review> findByUserId(Long userId, Pageable pageable);
    Optional<Review> findByUserIdAndProductId(Long userId, Long productId);

    //specific queries
    @Query("""
        SELECT COALESCE(AVG(r.rating), 0)
        FROM Review r
        WHERE r.product.id = :productId
    """)
    Double getAverageRatingByProductId(Long productId);

    Long countByProductId(Long productId);

    Long countByProductCompanyId(Long companyId);

    List<Review> findByProductCompanyId(Long companyId);

    @Query("""
    SELECT AVG(r.rating)
    FROM Review r
    WHERE r.product.company.id = :companyId
    """)
    Double findAverageRatingByCompanyId(Long companyId);

}