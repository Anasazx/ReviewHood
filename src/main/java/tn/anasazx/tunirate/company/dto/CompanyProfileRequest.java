package tn.anasazx.tunirate.company.dto;

import jakarta.validation.constraints.NotNull;
import tn.anasazx.tunirate.companySocialLink.dto.CompanySocialLinkRequest;
import tn.anasazx.tunirate.enums.Country;
import tn.anasazx.tunirate.enums.Industry;

import java.util.List;

// What a company head can change about their own company; name and status stay with admins
public record CompanyProfileRequest(
        String description,
        String phoneNumber,
        String address,
        @NotNull Country country,
        Industry industry,
        List<CompanySocialLinkRequest> socialLinks
) {}
