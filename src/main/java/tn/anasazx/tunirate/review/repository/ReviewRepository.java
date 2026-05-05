package tn.anasazx.tunirate.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.anasazx.tunirate.review.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByProductId(Long productId);

    List<Review> findAllByUserId(Long userId);

    Optional<Review> findByUserIdAndProductId(Long userId, Long productId);
}

