package tn.anasazx.tunirate.comment.repository;

import aj.org.objectweb.asm.commons.Remapper;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.anasazx.tunirate.comment.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByReview_Id(Long reviewId);

    List<Comment> findByUser_Id(Long userId);

    List<Comment> findByParentComment_Id(Long parentCommentId);

    List<Comment> findByReview_IdAndParentCommentIsNull(Long reviewId);

    Long countByReview_Id(Long reviewId);

    Optional<Comment> findFirstByReview_IdOrderByCreatedAtAsc(Long reviewId);

}