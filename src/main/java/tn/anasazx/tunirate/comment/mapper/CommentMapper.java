package tn.anasazx.tunirate.comment.mapper;

import tn.anasazx.tunirate.comment.dto.CommentResponse;
import tn.anasazx.tunirate.comment.entity.Comment;
import tn.anasazx.tunirate.security.SecurityUtils;

import java.util.Objects;

public class CommentMapper {

    public static CommentResponse toResponse(Comment comment) {

        Long currentUserId = SecurityUtils.getCurrentUserId();

        boolean isMine = currentUserId != null && Objects.equals(comment.getUser().getId(), currentUserId);

        Long parentId = comment.getParentComment() != null ? comment.getParentComment().getId() : null;

        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getName(),
                comment.getReview().getId(),
                parentId,
                isMine,
                comment.getCreatedAt()
        );
    }
}