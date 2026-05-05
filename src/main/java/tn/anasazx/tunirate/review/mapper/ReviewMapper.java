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
}

