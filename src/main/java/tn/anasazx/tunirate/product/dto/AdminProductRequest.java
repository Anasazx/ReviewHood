package tn.anasazx.tunirate.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tn.anasazx.tunirate.enums.ProductStatus;

public record AdminProductRequest(
        @NotBlank String name,
        String description,
        Long subcategoryId,
        @NotNull Long companyId,
        ProductStatus status
) {}