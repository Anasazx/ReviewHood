package tn.anasazx.tunirate.comment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.anasazx.tunirate.comment.dto.CommentResponse;


public interface CommentService {

    Page<CommentResponse> getCommentsByReviewId(Long reviewId, Pageable pageable, Long currentUserId);

    CommentResponse createComment(Long reviewId, String content, Long currentUserId);

    CommentResponse createCommentAsCompany(Long reviewId, String content, Long currentUserId);

    CommentResponse replyToComment(Long parentCommentId, String content, Long currentUserId);
    CommentResponse replyToCommentAsCompany(Long parentCommentId, String content, Long currentUserId);

    void deleteComment(Long commentId, Long currentUserId);

}