package tn.anasazx.tunirate.comment.mapper;

import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.actor.entity.Actor;
import tn.anasazx.tunirate.comment.dto.CommentResponse;
import tn.anasazx.tunirate.comment.entity.Comment;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.user.entity.User;

import java.util.List;

@Component
public class CommentMapper {

    public List<CommentResponse> toResponseList(List<Comment> comments, Long currentUserId) {
        return comments.stream()
                .map(c -> toResponse(c, currentUserId))
                .toList();
    }

    public CommentResponse toResponse(Comment comment, Long currentUserId) {
        Actor actor = comment.getActor();

        Comment repliedTo = comment.getRepliedTo();
        Actor mentionedActor = repliedTo != null ? repliedTo.getActor() : null;

        String authorName = switch (actor.getType()) {
            case USER -> ((User) actor).getName();
            case COMPANY -> ((Company) actor).getName();
            default -> "Unknown";
        };

        boolean isMine = switch (actor.getType()) {
            case USER -> actor.getId().equals(currentUserId);
            case COMPANY -> false;
            default -> false;
        };

        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                authorName,
                actor.getType(),
                actor.getId(),
                comment.getReview().getId(),
                repliedTo != null ? repliedTo.getId() : null,
                mentionedActor != null ? mentionedActor.getId() : null,
                mentionedActor != null ? mentionedActor.getType() : null,
                isMine,
                comment.getCreatedAt()
        );
    }
}