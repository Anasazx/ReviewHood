package tn.anasazx.tunirate.company.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.companySocialLink.mapper.CompanySocialLinkMapper;
import tn.anasazx.tunirate.company.dto.AdminCompanyResponse;
import tn.anasazx.tunirate.company.dto.AdminCompanySummaryResponse;
import tn.anasazx.tunirate.company.dto.CompanyDetailResponse;
import tn.anasazx.tunirate.company.dto.CompanyResponse;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.membership.mapper.CompanyMemberMapper;
import tn.anasazx.tunirate.product.mapper.ProductMapper;
import tn.anasazx.tunirate.subcategory.dto.SubcategoryResponse;
import tn.anasazx.tunirate.user.dto.UserResponse;
import tn.anasazx.tunirate.user.mapper.UserMapper;

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
                company.getStatus(),
                company.isVerified()
        );
    }

    public CompanyDetailResponse toDetailResponse(Company company, List<SubcategoryResponse> subcategories,
                                                  double averageRating, long reviewCount, long productCount) {
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
                company.getStatus(),
                company.isVerified(),
                averageRating,
                reviewCount,
                productCount
        );
    }

    public AdminCompanyResponse toAdminResponse(Company company, long productCount) {

        UserResponse verifiedBy = company.getVerifiedBy() != null ? UserMapper.toResponse(company.getVerifiedBy()) : null;
        UserResponse createdBy = company.getCreatedBy() != null ? UserMapper.toResponse(company.getCreatedBy()) : null;


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
                company.getVerifiedAt(),
                createdBy,
                company.getCreatedAt(),
                company.getMembers().stream().map(companyMemberMapper::toResponse).toList(),
                productCount
        );
    }

    public AdminCompanySummaryResponse toAdminSummaryResponse(Company company, long productCount, long memberCount) {
        return new AdminCompanySummaryResponse(
                company.getId(),
                company.getName(),
                company.getLogoUrl(),
                company.getCountry(),
                company.getIndustry(),
                company.getStatus(),
                productCount,
                memberCount,
                company.getCreatedAt()
        );
    }

}


