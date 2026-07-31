package tn.anasazx.tunirate.comment.mapper;

import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.actor.entity.Actor;
import tn.anasazx.tunirate.comment.dto.CommentResponse;
import tn.anasazx.tunirate.comment.entity.Comment;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.user.entity.User;


@Component
public class CommentMapper {

    public static CommentResponse toResponse(Comment comment, boolean liked) {

        Actor actor = comment.getActor();

        Comment repliedTo = comment.getRepliedTo();

        String actorName = switch (actor.getType()) {
            case USER -> ((User) actor).getName();
            case COMPANY -> ((Company) actor).getName();
            default -> "Unknown";
        };

        Actor mentionedActor = repliedTo != null ? repliedTo.getActor() : null;

        String repliedToActorName = null;

        if (mentionedActor != null) {
            repliedToActorName = switch (mentionedActor.getType()) {
                case USER -> ((User) mentionedActor).getName();
                case COMPANY -> ((Company) mentionedActor).getName();
                default -> "Unknown";
            };
        }

        String actorAvatarUrl = switch (actor.getType()) {
            case USER -> ((User) actor).getAvatarUrl();
            case COMPANY -> ((Company) actor).getLogoUrl();
            default -> null;
        };

        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                actorName,
                actor.getType(),
                actor.getId(),
                actorAvatarUrl,
                comment.getReview().getId(),
                repliedTo != null ? repliedTo.getId() : null,
                mentionedActor != null ? mentionedActor.getId() : null,
                mentionedActor != null ? mentionedActor.getType() : null,
                repliedToActorName,
                comment.getLikeCount(),
                liked,
                comment.getCreatedAt()
        );
    }
}