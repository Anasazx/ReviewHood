package tn.anasazx.tunirate.company.service;

import tn.anasazx.tunirate.company.dto.CompanyRequest;
import tn.anasazx.tunirate.company.dto.CompanyResponse;

import java.util.List;

public interface CompanyService {

    CompanyResponse getCompanyById(Long id);

    CompanyResponse getCompanyByName(String name);

    List<CompanyResponse> getAllCompanies();

    CompanyResponse createCompany(CompanyRequest request);

    CompanyResponse updateCompany(Long id, CompanyRequest request);

    void deleteCompany(Long id);

    CompanyResponse getCompanyByProductId(Long productId);
}

