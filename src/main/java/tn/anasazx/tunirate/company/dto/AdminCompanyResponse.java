package tn.anasazx.tunirate.company.dto;

import tn.anasazx.tunirate.companySocialLink.dto.CompanySocialLinkResponse;
import tn.anasazx.tunirate.enums.CompanyStatus;
import tn.anasazx.tunirate.enums.Country;
import tn.anasazx.tunirate.enums.Industry;
import tn.anasazx.tunirate.membership.dto.CompanyMemberResponse;
import tn.anasazx.tunirate.product.dto.AdminProductResponse;
import tn.anasazx.tunirate.user.dto.UserResponse;

import java.time.LocalDateTime;
import java.util.List;

public record AdminCompanyResponse(
        Long id,
        String name,
        String description,
        String logoUrl,
        String bannerUrl,
        String phoneNumber,
        List<CompanySocialLinkResponse> socialLinks,
        String address,
        Country country,
        Industry industry,
        CompanyStatus status,
        UserResponse verifiedBy,
        LocalDateTime verifiedAt,
        UserResponse createdBy,
        LocalDateTime createdAt,
        List<CompanyMemberResponse> members,
        List<AdminProductResponse> products
) {}