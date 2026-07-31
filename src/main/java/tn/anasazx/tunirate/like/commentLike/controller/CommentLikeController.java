package tn.anasazx.tunirate.like.commentLike.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.anasazx.tunirate.like.commentLike.dto.LikeStatusDto;
import tn.anasazx.tunirate.like.commentLike.service.CommentLikeService;
import tn.anasazx.tunirate.security.SecurityUtils;

@RestController
@RequestMapping("/comments/{commentId}/likes")
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping
    public ResponseEntity<LikeStatusDto> addLike(@PathVariable Long commentId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        commentLikeService.addLike(commentId, currentUserId);
        return ResponseEntity.ok(new LikeStatusDto(true));
    }

    @DeleteMapping
    public ResponseEntity<LikeStatusDto> removeLike(@PathVariable Long commentId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        commentLikeService.removeLike(commentId, currentUserId);
        return ResponseEntity.ok(new LikeStatusDto(false));
    }

    @GetMapping
    public ResponseEntity<LikeStatusDto> status(@PathVariable Long commentId) {
        Long currentUserId = SecurityUtils.getCurrentUserIdOrNull();
        boolean liked = currentUserId != null && commentLikeService.isLikedByUser(commentId, currentUserId);
        return ResponseEntity.ok(new LikeStatusDto(liked));
    }
}