package tn.anasazx.tunirate.company.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.company.dto.CompanyRequest;
import tn.anasazx.tunirate.company.dto.CompanyResponse;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.company.mapper.CompanyMapper;
import tn.anasazx.tunirate.company.repository.CompanyRepository;
import tn.anasazx.tunirate.company.service.CompanyService;
import tn.anasazx.tunirate.product.entity.Product;
import tn.anasazx.tunirate.product.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    private final CompanyMapper companyMapper;

    @Override
    public CompanyResponse getCompanyById(Long id) {
        return companyMapper.toResponse(findCompany(id));
    }

    @Override
    public CompanyResponse getCompanyByName(String name) {
        Company company = companyRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));

        return companyMapper.toResponse(company);
    }

    @Override
    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll().stream().map(companyMapper::toResponse).toList();
    }

    @Override
    public CompanyResponse createCompany(CompanyRequest request) {
        companyRepository.findByNameIgnoreCase(request.name()).ifPresent(existing -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Company name already exists");
        });

        Company company = new Company();
        company.setName(request.name());
        company.setDescription(request.description());
        company.setVerified(request.verified() != null ? request.verified() : false);

        return companyMapper.toResponse(companyRepository.save(company));
    }

    @Override
    public CompanyResponse updateCompany(Long id, CompanyRequest request) {
        Company company = findCompany(id);

        companyRepository.findByNameIgnoreCase(request.name())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Company name already exists");
                });

        company.setName(request.name());
        company.setDescription(request.description());
        company.setVerified(request.verified() != null ? request.verified() : company.getVerified());

        return companyMapper.toResponse(companyRepository.save(company));
    }

    @Override
    public void deleteCompany(Long id) {
        companyRepository.delete(findCompany(id));
    }

    @Override
    public CompanyResponse getCompanyByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        return companyMapper.toResponse(product.getCompany());
    }

    private Company findCompany(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }
}

