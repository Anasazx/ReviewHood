package tn.anasazx.tunirate.company.service;

import org.springframework.web.multipart.MultipartFile;
import tn.anasazx.tunirate.company.dto.AdminCompanyRequest;
import tn.anasazx.tunirate.company.dto.AdminCompanyResponse;
import tn.anasazx.tunirate.company.dto.CompanyDetailResponse;
import tn.anasazx.tunirate.company.dto.CompanyResponse;
import tn.anasazx.tunirate.enums.CompanyStatus;

import java.util.List;

public interface CompanyService {

    CompanyResponse getCompanyById(Long companyId);

    CompanyDetailResponse getCompanyDetailsById(Long companyId);

    CompanyResponse getMyCompany();

    CompanyResponse getCompanyByName(String companyName);

    List<CompanyResponse> getAllCompanies();

    //Admin method
    AdminCompanyResponse getCompanyDetailsByIdAsAdmin(Long companyId);


    CompanyResponse createCompanyAsAdmin(AdminCompanyRequest request, MultipartFile logo, MultipartFile banner);

    CompanyResponse updateCompanyAsAdmin(Long companyId, AdminCompanyRequest request, MultipartFile logo, MultipartFile banner);

    void updateCompanyStatusAsAdmin(Long companyId, CompanyStatus companyStatus);

    CompanyResponse getCompanyByProductId(Long productId);

    List<CompanyResponse> search(String query);

}

