package tn.anasazx.tunirate.comment.dto;


import lombok.Builder;
import tn.anasazx.tunirate.enums.ActorType;

import java.time.LocalDateTime;

@Builder
public record CommentResponse(
        Long id,
        String content,
        String actorName,
        ActorType actorType,
        Long actorId,
        String actorAvatarUrl,
        Long reviewId,
        Long repliedToCommentId,
        Long repliedToActorId,
        ActorType repliedToActorType,
        String repliedToActorName,
        Long likeCount,
        boolean liked,
        boolean deleted,
        LocalDateTime createdAt
) {}

