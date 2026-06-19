package tn.anasazx.tunirate.review.dto;

import tn.anasazx.tunirate.comment.dto.CommentResponse;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Integer rating,
        String content,
        String userName,
        boolean isMine,
        Long commentsCount,
        CommentResponse previewComment,
        LocalDateTime createdAt
) {}

