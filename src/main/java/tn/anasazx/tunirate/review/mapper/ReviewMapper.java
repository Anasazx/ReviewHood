package tn.anasazx.tunirate.review.mapper;

import tn.anasazx.tunirate.comment.dto.CommentResponse;
import tn.anasazx.tunirate.review.dto.MinimizedReviewResponse;
import tn.anasazx.tunirate.review.dto.ReviewResponse;
import tn.anasazx.tunirate.review.entity.Review;
import tn.anasazx.tunirate.user.mapper.UserMapper;


public class ReviewMapper {

    public static ReviewResponse toResponse(Review review, Long commentsCount, CommentResponse previewComment) {
        return new ReviewResponse(
                review.getId(),
                review.getRating(),
                review.getContent(),
                UserMapper.toMinimizedResponse(review.getUser()),
                commentsCount,
                previewComment,
                review.getCreatedAt()
        );
    }

    public static MinimizedReviewResponse toMinimizedResponse(Review review) {
        return new MinimizedReviewResponse(
                review.getId(),
                review.getRating(),
                review.getContent(),
                review.getUser().getName(),
                review.getProduct().getName(),
                review.getProduct().getId(),
                review.getCreatedAt()
        );
    }

}

