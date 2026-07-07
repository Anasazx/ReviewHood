package tn.anasazx.tunirate.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.anasazx.tunirate.comment.entity.Comment;
import tn.anasazx.tunirate.enums.ActorType;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByReview_Id(Long reviewId, Pageable pageable);

    Long countByReview_Id(Long reviewId);

    Optional<Comment> findFirstByReview_IdAndActor_TypeAndActor_IdOrderByCreatedAtDesc(Long reviewId, ActorType actorType, Long actorId);

}