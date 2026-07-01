package tn.anasazx.tunirate.product.dto;

import tn.anasazx.tunirate.enums.ProductStatus;

import java.time.LocalDateTime;

public record AdminProductResponse(
        Long id,
        String name,
        String description,
        String category,
        Long subcategoryId,
        String subcategoryName,
        Long companyId,
        String companyName,
        boolean companyIsVerified,
        String companyLogoUrl,
        String imageUrl,
        LocalDateTime createdAt,
        String createdByName,
        Long createdById,
        String updatedByName,
        Long updatedById,
        ProductStatus status
) {}