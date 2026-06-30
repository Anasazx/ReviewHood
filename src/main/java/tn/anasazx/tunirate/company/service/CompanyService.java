package tn.anasazx.tunirate.company.service;

import org.springframework.web.multipart.MultipartFile;
import tn.anasazx.tunirate.company.dto.AdminCompanyRequest;
import tn.anasazx.tunirate.company.dto.AdminCompanyResponse;
import tn.anasazx.tunirate.company.dto.CompanyResponse;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.enums.CompanyImageType;

import java.util.List;

public interface CompanyService {

    CompanyResponse getCompanyById(Long companyId);

    CompanyResponse getMyCompany();

    CompanyResponse getCompanyByName(String companyName);

    List<CompanyResponse> getAllCompanies();

    //Admin method
    AdminCompanyResponse getCompanyDetailsByIdAsAdmin(Long companyId);


    CompanyResponse createCompanyAsAdmin(AdminCompanyRequest request);

    CompanyResponse updateCompanyAsAdmin(Long companyId, AdminCompanyRequest request);

    void deleteCompany(Long companyId);

    CompanyResponse getCompanyByProductId(Long productId);

    Company getCompanyEntityById(Long companyId);

    List<CompanyResponse> search(String query);

    //FOR LOGO AND BANNER :
    void uploadImage(Long companyId, MultipartFile file, CompanyImageType type);
    void deleteImage(Long companyId, CompanyImageType type);
}

