package tn.anasazx.tunirate.like.commentLike.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.anasazx.tunirate.like.commentLike.entity.CommentLike;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    boolean existsByComment_IdAndUser_Id(Long commentId, Long userId);
    Optional<CommentLike> findByComment_IdAndUser_Id(Long commentId, Long userId);
}
