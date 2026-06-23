package tn.anasazx.tunirate.comment.service;

import tn.anasazx.tunirate.comment.dto.CommentResponse;

import java.util.List;

public interface CommentService {

    List<CommentResponse> getCommentsByReviewId(Long reviewId);

    CommentResponse createComment(Long reviewId, String content);

    CommentResponse createCommentAsCompany(Long reviewId, String content);

    CommentResponse replyToComment(Long parentCommentId, String content);

    void deleteComment(Long commentId);

}