package tn.anasazx.tunirate.review.dto;

import tn.anasazx.tunirate.comment.dto.CommentResponse;
import tn.anasazx.tunirate.user.dto.MinimizedUserResponse;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Integer rating,
        String content,
        MinimizedUserResponse user,
        Long commentsCount,
        CommentResponse previewComment,
        LocalDateTime createdAt
) {}

