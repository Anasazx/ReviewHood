package tn.anasazx.tunirate.company.service;

import org.springframework.web.multipart.MultipartFile;
import tn.anasazx.tunirate.company.dto.CompanyRequest;
import tn.anasazx.tunirate.company.dto.CompanyResponse;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.enums.CompanyImageType;

import java.util.List;

public interface CompanyService {

    CompanyResponse getCompanyById(Long id);

    CompanyResponse getMyCompany();

    CompanyResponse getCompanyByName(String name);

    List<CompanyResponse> getAllCompanies();

    CompanyResponse createCompany(CompanyRequest request);

    CompanyResponse updateCompany(Long id, CompanyRequest request);

    void deleteCompany(Long id);

    CompanyResponse getCompanyByProductId(Long productId);

    Company getCompanyEntityById(Long id);

    List<CompanyResponse> search(String query);

    //FOR LOGO AND BANNER :
    void uploadImage(Long companyId, MultipartFile file, CompanyImageType type);
    void deleteImage(Long companyId, CompanyImageType type);
}

