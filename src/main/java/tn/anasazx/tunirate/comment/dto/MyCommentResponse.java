package tn.anasazx.tunirate.comment.dto;

import java.time.LocalDateTime;

// One of the signed-in user's comments, with the product it is about
public record MyCommentResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        Long likeCount,
        Long reviewId,
        Long productId,
        String productName,
        // who the comment answers, null when it answers the review itself
        String repliedToName
) {}
