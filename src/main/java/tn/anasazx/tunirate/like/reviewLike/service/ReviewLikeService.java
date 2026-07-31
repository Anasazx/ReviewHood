package tn.anasazx.tunirate.like.reviewLike.service;

public interface ReviewLikeService {
    void addLike(Long reviewId, Long userId);
    void removeLike(Long reviewId, Long userId);
    boolean isLikedByUser(Long reviewId, Long userId);
    long countLikes(Long reviewId);
}