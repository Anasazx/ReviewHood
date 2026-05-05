package tn.anasazx.tunirate.review.dto;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Integer rating,
        String content,
        String userName,
        LocalDateTime createdAt
) {
}

