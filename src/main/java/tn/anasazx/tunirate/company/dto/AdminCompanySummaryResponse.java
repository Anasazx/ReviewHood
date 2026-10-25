package tn.anasazx.tunirate.company.dto;

import tn.anasazx.tunirate.enums.CompanyStatus;
import tn.anasazx.tunirate.enums.Country;
import tn.anasazx.tunirate.enums.Industry;

import java.time.LocalDateTime;

// One row of the admin companies table
public record AdminCompanySummaryResponse(
        Long id,
        String name,
        String logoUrl,
        Country country,
        Industry industry,
        CompanyStatus status,
        long productCount,
        long memberCount,
        LocalDateTime createdAt
) {}
