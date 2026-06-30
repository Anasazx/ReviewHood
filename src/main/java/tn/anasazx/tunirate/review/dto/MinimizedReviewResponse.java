package tn.anasazx.tunirate.review.dto;

import java.time.LocalDateTime;

public record MinimizedReviewResponse(
        Long id,
        Integer rating,
        String content,
        String userName,
        String productName,
        LocalDateTime createdAt
) {}