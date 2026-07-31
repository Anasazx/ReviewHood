package tn.anasazx.tunirate.like.commentLike.service.implementation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import tn.anasazx.tunirate.comment.repository.CommentRepository;
import tn.anasazx.tunirate.like.commentLike.entity.CommentLike;
import tn.anasazx.tunirate.like.commentLike.repository.CommentLikeRepository;
import tn.anasazx.tunirate.like.commentLike.service.CommentLikeService;
import tn.anasazx.tunirate.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void addLike(Long commentId, Long userId) {

        if (commentId == null || userId == null) {
            return;
        }

        if (commentLikeRepository.existsByComment_IdAndUser_Id(commentId, userId)) {
            return; // already liked, no-op
        }

        CommentLike like = CommentLike.builder()
                .comment(commentRepository.getReferenceById(commentId))
                .user(userRepository.getReferenceById(userId))
                .build();

        try {
            commentLikeRepository.save(like);
            commentRepository.incrementLikeCount(commentId);
        } catch (DataIntegrityViolationException e) {
            // race condition: another request already liked it concurrently, safe to ignore
        }
    }

    @Transactional
    @Override
    public void removeLike(Long commentId, Long userId) {

        commentLikeRepository
                .findByComment_IdAndUser_Id(commentId, userId)
                .ifPresent(like -> {
                    commentLikeRepository.delete(like);
                    commentRepository.decrementLikeCount(commentId);
                });
    }

    @Override
    public boolean isLikedByUser(Long commentId, Long userId) {
        return commentLikeRepository.existsByComment_IdAndUser_Id(commentId, userId);
    }

    @Override
    public long countLikes(Long commentId) {
        return commentRepository.findLikeCountById(commentId)
                .orElse(0L);
    }
}