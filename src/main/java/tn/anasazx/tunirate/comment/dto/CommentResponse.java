package tn.anasazx.tunirate.comment.dto;


import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        String userName,
        Long reviewId,
        Long parentCommentId,
        boolean isMine,
        LocalDateTime createdAt
) {}