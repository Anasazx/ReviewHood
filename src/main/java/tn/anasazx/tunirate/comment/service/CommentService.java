package tn.anasazx.tunirate.comment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.anasazx.tunirate.comment.dto.CommentResponse;


public interface CommentService {

    Page<CommentResponse> getCommentsByReviewId(Long reviewId, Pageable pageable);

    CommentResponse createComment(Long reviewId, String content);

    CommentResponse createCommentAsCompany(Long reviewId, String content);

    CommentResponse replyToComment(Long parentCommentId, String content);
    CommentResponse replyToCommentAsCompany(Long parentCommentId, String content);

    void deleteComment(Long commentId);

}