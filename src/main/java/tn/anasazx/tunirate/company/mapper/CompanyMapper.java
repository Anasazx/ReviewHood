package tn.anasazx.tunirate.company.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.companySocialLink.mapper.CompanySocialLinkMapper;
import tn.anasazx.tunirate.company.dto.AdminCompanyResponse;
import tn.anasazx.tunirate.company.dto.CompanyDetailResponse;
import tn.anasazx.tunirate.company.dto.CompanyResponse;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.membership.mapper.CompanyMemberMapper;
import tn.anasazx.tunirate.product.mapper.ProductMapper;
import tn.anasazx.tunirate.subcategory.dto.SubcategoryResponse;
import tn.anasazx.tunirate.user.dto.UserResponse;
import tn.anasazx.tunirate.user.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CompanyMapper {

    private final ProductMapper productMapper;
    private final CompanyMemberMapper companyMemberMapper;

    public CompanyResponse toResponse(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getDescription(),
                company.getLogoUrl(),
                company.getBannerUrl(),
                company.getCountry(),
                company.getStatus()
        );
    }

    public CompanyDetailResponse toDetailResponse(Company company, List<SubcategoryResponse> subcategories) {
        return new CompanyDetailResponse(
                company.getId(),
                company.getName(),
                company.getDescription(),
                company.getLogoUrl(),
                company.getBannerUrl(),
                company.getPhoneNumber(),
                company.getAddress(),
                company.getCountry(),
                company.getIndustry(),
                subcategories,
                company.getSocialLinks().stream().map(CompanySocialLinkMapper::toResponse).toList(),
                company.getStatus()
        );
    }

    public AdminCompanyResponse toAdminResponse(Company company) {

        UserResponse verifiedBy = null;
        LocalDateTime verifiedAt = null;

        if (company.getVerifiedBy() != null) {
            verifiedBy = UserMapper.toResponse(company.getVerifiedBy());
            verifiedAt = LocalDateTime.now();
        }


        return new AdminCompanyResponse(
                company.getId(),
                company.getName(),
                company.getDescription(),
                company.getLogoUrl(),
                company.getBannerUrl(),
                company.getPhoneNumber(),
                company.getSocialLinks().stream().map(CompanySocialLinkMapper::toResponse).toList(),
                company.getAddress(),
                company.getCountry(),
                company.getIndustry(),
                company.getStatus(),
                verifiedBy,
                verifiedAt,
                UserMapper.toResponse(company.getCreatedBy()),
                company.getCreatedAt(),
                company.getMembers().stream().map(companyMemberMapper::toResponse).toList(),
                company.getProducts().stream().map(productMapper::toAdminResponse).toList()
        );
    }

}


