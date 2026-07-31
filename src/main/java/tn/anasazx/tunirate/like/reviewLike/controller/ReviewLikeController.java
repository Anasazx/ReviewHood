package tn.anasazx.tunirate.like.reviewLike.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.anasazx.tunirate.like.reviewLike.dto.LikeStatusDto;
import tn.anasazx.tunirate.like.reviewLike.service.ReviewLikeService;
import tn.anasazx.tunirate.security.SecurityUtils;


@RestController
@RequestMapping("/reviews/{reviewId}/likes")
@RequiredArgsConstructor
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    @PostMapping
    public ResponseEntity<LikeStatusDto> addLike(@PathVariable Long reviewId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        reviewLikeService.addLike(reviewId, currentUserId);
        return ResponseEntity.ok(
                new LikeStatusDto(true)
        );
    }

    @DeleteMapping
    public ResponseEntity<LikeStatusDto> removeLike(@PathVariable Long reviewId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        reviewLikeService.removeLike(reviewId, currentUserId);
        return ResponseEntity.ok(new LikeStatusDto(false));
    }

    @GetMapping
    public ResponseEntity<LikeStatusDto> status(@PathVariable Long reviewId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(new LikeStatusDto(reviewLikeService.isLikedByUser(reviewId, currentUserId)));
    }

}