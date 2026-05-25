package tn.anasazx.tunirate.product.dto;


import java.time.LocalDateTime;

public record ProductResponse(
        Long id,
        String name,
        String description,
        String category,
        String subcategory,
        Long companyId,
        String companyName,
        String imageUrl,
        LocalDateTime createdAt
) {}