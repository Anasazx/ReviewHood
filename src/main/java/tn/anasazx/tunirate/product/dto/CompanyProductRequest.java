package tn.anasazx.tunirate.product.dto;

import jakarta.validation.constraints.NotBlank;
import tn.anasazx.tunirate.enums.ProductStatus;

public record CompanyProductRequest(
        @NotBlank String name,
        String description,
        Long subcategoryId,
        ProductStatus status
) {}
