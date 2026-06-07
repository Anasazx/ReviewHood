package tn.anasazx.tunirate.review.mapper;

import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.review.dto.ReviewResponse;
import tn.anasazx.tunirate.review.entity.Review;



@Component
public class ReviewMapper {

    public ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getRating(),
                review.getContent(),
                review.getUser().getName(),
                review.getCreatedAt()
        );
    }

    public ReviewResponse toResponse(Review review, boolean isMine) {
        return new ReviewResponse(
                review.getId(),
                review.getRating(),
                review.getContent(),
                review.getUser().getName(),
                isMine,
                review.getCreatedAt()
        );
    }


    public ReviewResponse toProductResponse(Review review, Long userId) {
        return new ReviewResponse(
                review.getId(),
                review.getRating(),
                review.getContent(),
                review.getUser().getName(),
                review.getUser().getId().equals(userId),
                review.getCreatedAt()
        );
    }


}

