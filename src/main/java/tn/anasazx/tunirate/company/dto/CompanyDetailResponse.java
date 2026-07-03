package tn.anasazx.tunirate.company.dto;

import tn.anasazx.tunirate.CompanySocialLink.dto.CompanySocialLinkResponse;
import tn.anasazx.tunirate.enums.CompanyStatus;
import tn.anasazx.tunirate.enums.Country;
import tn.anasazx.tunirate.enums.Industry;
import tn.anasazx.tunirate.product.dto.ProductResponse;

import java.util.List;

public record CompanyDetailResponse(
        Long id,
        String name,
        String description,
        String logoUrl,
        String bannerUrl,
        String phoneNumber,
        String websiteUrl,
        String address,
        Country country,
        Industry industry,
        List<CompanySocialLinkResponse> socialLinks,
        List<ProductResponse> products,
        CompanyStatus status
) {}