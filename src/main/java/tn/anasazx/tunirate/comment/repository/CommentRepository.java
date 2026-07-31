package tn.anasazx.tunirate.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.anasazx.tunirate.comment.entity.Comment;
import tn.anasazx.tunirate.enums.ActorType;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByReview_Id(Long reviewId, Pageable pageable);

    Long countByReview_Id(Long reviewId);

    Optional<Comment> findFirstByReview_IdAndActor_TypeAndActor_IdOrderByCreatedAtDesc(Long reviewId, ActorType actorType, Long actorId);

    @Modifying
    @Query("UPDATE Comment c SET c.likeCount = c.likeCount + 1 WHERE c.id = :commentId")
    void incrementLikeCount(@Param("commentId") Long commentId);

    @Modifying
    @Query("UPDATE Comment c SET c.likeCount = c.likeCount - 1 WHERE c.id = :commentId AND c.likeCount > 0")
    void decrementLikeCount(@Param("commentId") Long commentId);

    @Query("SELECT c.likeCount FROM Comment c WHERE c.id = :commentId")
    Optional<Long> findLikeCountById(@Param("commentId") Long commentId);


    @Modifying
    @Query(value = """
    UPDATE comments c
    SET like_count = (SELECT COUNT(*) FROM comment_likes cl WHERE cl.comment_id = c.id)
    WHERE c.like_count != (SELECT COUNT(*) FROM comment_likes cl WHERE cl.comment_id = c.id)
    """, nativeQuery = true)
    int reconcileAllLikeCounts();

}