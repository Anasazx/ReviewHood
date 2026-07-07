package tn.anasazx.tunirate.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tn.anasazx.tunirate.companySocialLink.dto.CompanySocialLinkRequest;
import tn.anasazx.tunirate.enums.CompanyStatus;
import tn.anasazx.tunirate.enums.Country;
import tn.anasazx.tunirate.enums.Industry;

import java.util.List;


public record AdminCompanyRequest(
        @NotBlank String name,
        String description,
        String phoneNumber,
        String websiteUrl,
        String address,
        @NotNull Country country,
        Industry industry,
        List<CompanySocialLinkRequest> socialLinks,
        CompanyStatus status
) {}