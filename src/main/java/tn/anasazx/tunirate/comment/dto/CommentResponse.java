package tn.anasazx.tunirate.comment.dto;


import tn.anasazx.tunirate.enums.ActorType;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        String actorName,    // ← rename from userName, covers both User and Company
        ActorType actorType,// ← lets the frontend know how to render it
        Long actorId,
        Long reviewId,
        Long repliedToCommentId,
        Long repliedToActorId,
        ActorType repliedToActorType,
        String repliedToActorName,
        boolean isMine,
        LocalDateTime createdAt
) {}

