package tn.anasazx.tunirate.comment.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.anasazx.tunirate.comment.dto.CommentRequest;
import tn.anasazx.tunirate.comment.dto.CommentResponse;
import tn.anasazx.tunirate.comment.service.CommentService;


@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //Get all comments for a review
    @GetMapping("/review/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<CommentResponse> getCommentByReviewId(@PathVariable Long reviewId, Pageable pageable) {
        return commentService.getCommentsByReviewId(reviewId, pageable);
    }

    //TODO: Add get my comments endpoint - ofc with pagination

    //Create comment
    @PostMapping("/review/{reviewId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse createComment(@PathVariable Long reviewId, @RequestBody CommentRequest request) {
        return commentService.createComment(reviewId, request.content());
    }

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/c/reply/{parentCommentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse replyToCommentAsCompany(@PathVariable Long parentCommentId, @RequestBody CommentRequest request) {
        return commentService.replyToCommentAsCompany(parentCommentId, request.content());
    }

    //Delete comment
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }

}