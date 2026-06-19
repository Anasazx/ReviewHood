package tn.anasazx.tunirate.comment.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.comment.dto.CommentResponse;
import tn.anasazx.tunirate.comment.entity.Comment;
import tn.anasazx.tunirate.comment.mapper.CommentMapper;
import tn.anasazx.tunirate.comment.repository.CommentRepository;
import tn.anasazx.tunirate.comment.service.CommentService;
import tn.anasazx.tunirate.review.entity.Review;
import tn.anasazx.tunirate.review.repository.ReviewRepository;
import tn.anasazx.tunirate.security.SecurityUtils;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    public List<CommentResponse> getCommentsByReviewId(Long reviewId) {
        return commentRepository.findByReview_IdAndParentCommentIsNull(reviewId)
                .stream()
                .map(CommentMapper::toResponse)
                .toList();
    }

    @Override
    public CommentResponse createComment(Long reviewId, String content) {

        Long userId = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setReview(review);

        return CommentMapper.toResponse(commentRepository.save(comment));
    }

    @Override
    public CommentResponse replyToComment(Long parentCommentId, String content) {

        Long userId = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Comment parent = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent comment not found"));

        Comment reply = new Comment();
        reply.setContent(content);
        reply.setUser(user);
        reply.setReview(parent.getReview());
        reply.setParentComment(parent);

        return CommentMapper.toResponse(commentRepository.save(reply));
    }

    @Override
    public void deleteComment(Long commentId) {

        Long userId = SecurityUtils.getCurrentUserId();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your comment");
        }

        commentRepository.delete(comment);
    }

}