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
        boolean companyIsVerified,
        String companyLogoUrl,
        String imageUrl,
        LocalDateTime createdAt
) {}