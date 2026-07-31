package tn.anasazx.tunirate.like.commentLike.service;

import jakarta.transaction.Transactional;

public interface CommentLikeService {

    @Transactional
    void addLike(Long commentId, Long userId);

    @Transactional
    void removeLike(Long commentId, Long userId);

    boolean isLikedByUser(Long commentId, Long userId);

    long countLikes(Long commentId);
}