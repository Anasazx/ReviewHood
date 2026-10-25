package tn.anasazx.tunirate.comment.service;

import tn.anasazx.tunirate.comment.dto.MyCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.anasazx.tunirate.comment.dto.CommentResponse;


public interface CommentService {

    Page<MyCommentResponse> getMyComments(Long currentUserId, Pageable pageable);


    Page<CommentResponse> getCommentsByReviewId(Long reviewId, Pageable pageable, Long currentUserId);

    CommentResponse createComment(Long reviewId, String content, Long currentUserId);

    CommentResponse createCommentAsCompany(Long reviewId, String content, Long currentUserId);

    CommentResponse replyToComment(Long parentCommentId, String content, Long currentUserId);
    CommentResponse replyToCommentAsCompany(Long parentCommentId, String content, Long currentUserId);

    void deleteComment(Long commentId, Long currentUserId);

}