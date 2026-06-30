package tn.anasazx.tunirate.company.mapper;

import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.company.dto.AdminCompanyResponse;
import tn.anasazx.tunirate.company.dto.CompanyResponse;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.membership.mapper.CompanyMemberMapper;
import tn.anasazx.tunirate.product.mapper.ProductMapper;
import tn.anasazx.tunirate.user.dto.UserResponse;
import tn.anasazx.tunirate.user.mapper.UserMapper;

import java.time.LocalDateTime;

@Component
public class CompanyMapper {

    public static CompanyResponse toResponse(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getDescription(),
                company.getLogoUrl(),
                company.getBannerUrl(),
                company.getStatus()
        );
    }

    public static AdminCompanyResponse toAdminResponse(Company company) {

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
                company.getWebsiteUrl(),
                company.getSocialLinks(),
                company.getAddress(),
                company.getCountry(),
                company.getIndustry(),
                company.getStatus(),
                verifiedBy,
                verifiedAt,
                UserMapper.toResponse(company.getCreatedBy()),
                company.getCreatedAt(),
                company.getMembers().stream().map(CompanyMemberMapper::toResponse).toList(),
                company.getProducts().stream().map(ProductMapper::toAdminResponse).toList()
        );
    }

}


