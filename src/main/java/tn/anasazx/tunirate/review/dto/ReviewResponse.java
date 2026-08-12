package tn.anasazx.tunirate.review.dto;

import tn.anasazx.tunirate.comment.dto.CommentResponse;
import tn.anasazx.tunirate.user.dto.SecureMinimizedUserResponse;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Integer rating,
        String content,
        SecureMinimizedUserResponse user,
        Long commentsCount,
        CommentResponse previewComment,
        Long likeCount,
        boolean liked,
        LocalDateTime createdAt
) {}

