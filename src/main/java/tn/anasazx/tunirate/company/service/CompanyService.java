package tn.anasazx.tunirate.company.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    CompanyResponse getMyCompany(Long currentUserId);

    CompanyResponse getCompanyByName(String companyName);

    List<CompanyResponse> getAllCompanies();

    //Admin method
    AdminCompanyResponse getCompanyDetailsByIdAsAdmin(Long companyId);


    CompanyResponse createCompanyAsAdmin(AdminCompanyRequest request, MultipartFile logo, MultipartFile banner, Long currentUserId);

    CompanyResponse updateCompanyAsAdmin(Long companyId, AdminCompanyRequest request, MultipartFile logo, MultipartFile banner, Long currentUserId);

    void updateCompanyStatusAsAdmin(Long companyId, CompanyStatus companyStatus);

    CompanyResponse getCompanyByProductId(Long productId);

    List<CompanyResponse> search(String query);

    Page<CompanyResponse> getAllCompanies(Pageable pageable);
}

