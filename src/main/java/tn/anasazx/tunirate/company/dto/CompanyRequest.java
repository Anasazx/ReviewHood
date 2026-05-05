package tn.anasazx.tunirate.company.dto;

import jakarta.validation.constraints.NotBlank;

public record CompanyRequest(
        @NotBlank String name,
        String description,
        Boolean verified
) {
}

