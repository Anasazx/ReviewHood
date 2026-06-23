package tn.anasazx.tunirate.comment.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.anasazx.tunirate.comment.dto.CommentRequest;
import tn.anasazx.tunirate.comment.dto.CommentResponse;
import tn.anasazx.tunirate.comment.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //Get all comments for a review
    @GetMapping("/review/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponse> getCommentByReviewId(@PathVariable Long reviewId) {
        return commentService.getCommentsByReviewId(reviewId);
    }

    //Create comment
    @PostMapping("/review/{reviewId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse createComment(@PathVariable Long reviewId, @RequestBody CommentRequest request) {
        return commentService.createComment(reviewId, request.content());
    }

    @PostMapping("/c/review/{reviewId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse createCommentAsCompany(@PathVariable Long reviewId, @RequestBody CommentRequest request) {
        return commentService.createCommentAsCompany(reviewId, request.content());
    }

    @PostMapping("/reply/{parentCommentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse replyToComment(@PathVariable Long parentCommentId, @RequestBody CommentRequest request) {
        return commentService.replyToComment(parentCommentId, request.content());
    }

    @PostMapping("/c/reply/{parentCommentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse replyToCommentAsCompany(@PathVariable Long parentCommentId, @RequestBody CommentRequest request) {
        return commentService.replyToCommentAsCompany(parentCommentId, request.content());
    }

    //Delete comment
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }

}