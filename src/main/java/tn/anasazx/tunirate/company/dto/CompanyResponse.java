package tn.anasazx.tunirate.company.dto;

import tn.anasazx.tunirate.enums.CompanyStatus;

public record CompanyResponse(
        Long id,
        String name,
        String description,
        String logoUrl,
        String bannerUrl,
        CompanyStatus status
) {
}