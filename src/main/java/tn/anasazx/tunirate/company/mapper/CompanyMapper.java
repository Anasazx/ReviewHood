package tn.anasazx.tunirate.company.mapper;

import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.company.dto.AdminCompanyResponse;
import tn.anasazx.tunirate.company.dto.CompanyResponse;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.membership.mapper.CompanyMemberMapper;
import tn.anasazx.tunirate.product.mapper.ProductMapper;

@Component
public class CompanyMapper {

    public static CompanyResponse toResponse(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getDescription(),
                company.getLogoUrl(),
                company.getBannerUrl(),
                company.getVerified()
        );
    }

    public static AdminCompanyResponse toAdminResponse(Company company) {
        return new AdminCompanyResponse(
                company.getId(),
                company.getName(),
                company.getDescription(),
                company.getLogoUrl(),
                company.getBannerUrl(),
                company.getVerified(),
                company.getMembers().stream().map(CompanyMemberMapper::toResponse).toList(),
                company.getProducts().stream().map(ProductMapper::toAdminResponse).toList()
        );
    }

}

