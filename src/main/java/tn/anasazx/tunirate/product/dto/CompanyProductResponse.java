package tn.anasazx.tunirate.product.dto;

import tn.anasazx.tunirate.enums.ProductStatus;

import java.time.LocalDateTime;

public record CompanyProductResponse(
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
        LocalDateTime createdAt,
        String createdByName,
        String updatedByName,
        ProductStatus status
) {}