package tn.anasazx.tunirate.review.mapper;

import tn.anasazx.tunirate.comment.dto.CommentResponse;
import tn.anasazx.tunirate.review.dto.ReviewResponse;
import tn.anasazx.tunirate.review.entity.Review;
import tn.anasazx.tunirate.security.SecurityUtils;

import java.util.Objects;


public class ReviewMapper {

    public static ReviewResponse toResponse(Review review, Long commentsCount, CommentResponse previewComment) {

        Long currentUserId = SecurityUtils.getCurrentUserId();

        boolean isMine = currentUserId != null && Objects.equals(review.getUser().getId(), currentUserId);

        return new ReviewResponse(
                review.getId(),
                review.getRating(),
                review.getContent(),
                review.getUser().getName(),
                isMine,
                commentsCount,
                previewComment,
                review.getCreatedAt()
        );
    }

}

