package tn.anasazx.tunirate.review.dto;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Integer rating,
        String content,
        String userName,
        boolean isMine,
        LocalDateTime createdAt
) {

    //this is the constructor where i d'ont mention the review isMine or not
    public ReviewResponse(Long id, Integer rating, String content, String userName, LocalDateTime createdAt) {
        this(id, rating, content, userName, false, createdAt);
    }

}

