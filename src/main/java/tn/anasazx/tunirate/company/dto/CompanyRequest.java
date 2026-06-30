package tn.anasazx.tunirate.company.dto;

import jakarta.validation.constraints.NotBlank;
import tn.anasazx.tunirate.enums.CompanyStatus;

public record CompanyRequest(
        @NotBlank String name,
        String description,
        CompanyStatus status
) {
}

