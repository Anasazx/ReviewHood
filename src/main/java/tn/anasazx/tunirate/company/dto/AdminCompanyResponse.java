package tn.anasazx.tunirate.company.dto;

import tn.anasazx.tunirate.membership.dto.CompanyMemberResponse;
import tn.anasazx.tunirate.product.dto.AdminProductResponse;

import java.util.List;

public record AdminCompanyResponse(
        Long id,
        String name,
        String description,
        String logoUrl,
        String bannerUrl,
        Boolean verified,
        List<CompanyMemberResponse> members,
        List<AdminProductResponse> products
) {}