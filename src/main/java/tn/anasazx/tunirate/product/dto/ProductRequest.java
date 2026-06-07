package tn.anasazx.tunirate.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(
        @NotBlank String name,
        String description,
        Long subcategoryId,
        @NotNull Long companyId
) {}

