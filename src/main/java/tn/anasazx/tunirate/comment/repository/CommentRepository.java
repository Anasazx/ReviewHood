package tn.anasazx.tunirate.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.anasazx.tunirate.comment.entity.Comment;
import tn.anasazx.tunirate.enums.ActorType;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByReview_Id(Long reviewId);

    List<Comment> findByActor_Id(Long userId);

    List<Comment> findByRepliedTo_Id(Long parentCommentId);

    List<Comment> findByReview_IdAndRepliedToIsNull(Long reviewId);

    Long countByReview_Id(Long reviewId);

    Optional<Comment> findFirstByReview_IdOrderByCreatedAtAsc(Long reviewId);

    Optional<Comment> findFirstByReview_IdAndActor_TypeAndActor_IdOrderByCreatedAtDesc(Long reviewId, ActorType actorType, Long actorId);

}