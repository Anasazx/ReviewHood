package tn.anasazx.tunirate.company.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.company.dto.AdminCompanyResponse;
import tn.anasazx.tunirate.company.dto.CompanyRequest;
import tn.anasazx.tunirate.company.dto.CompanyResponse;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.company.mapper.CompanyMapper;
import tn.anasazx.tunirate.company.repository.CompanyRepository;
import tn.anasazx.tunirate.company.service.CompanyService;
import tn.anasazx.tunirate.enums.CompanyImageType;
import tn.anasazx.tunirate.fileStorage.service.FileStorageService;
import tn.anasazx.tunirate.membership.entity.CompanyMember;
import tn.anasazx.tunirate.membership.repository.CompanyMemberRepository;
import tn.anasazx.tunirate.product.entity.Product;
import tn.anasazx.tunirate.product.repository.ProductRepository;
import tn.anasazx.tunirate.security.SecurityUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    private final CompanyMemberRepository companyMemberRepository;
    private final FileStorageService fileStorageService;


    @Override
    public CompanyResponse getCompanyById(Long id) {
        return CompanyMapper.toResponse(findCompany(id));
    }

    @Override
    public CompanyResponse getMyCompany() {
        long currentUserId = SecurityUtils.getCurrentUserId();
        CompanyMember membership = companyMemberRepository.findFirstByUserId(currentUserId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User doesn't belong to any company!")
        );

        //Extract company entity from the membership response
        Company company = membership.getCompany();

        return CompanyMapper.toResponse(company);
    }


    @Override
    public CompanyResponse getCompanyByName(String name) {
        Company company = companyRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));

        return CompanyMapper.toResponse(company);
    }

    @Override
    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll().stream().map(CompanyMapper::toResponse).toList();
    }

    @Override
    public AdminCompanyResponse getCompanyDetailsByIdAsAdmin(Long companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found")
        );
        return CompanyMapper.toAdminResponse(company);
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

        return CompanyMapper.toResponse(companyRepository.save(company));
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

        return CompanyMapper.toResponse(companyRepository.save(company));
    }

    @Override
    public void deleteCompany(Long id) {
        companyRepository.delete(findCompany(id));
    }

    @Override
    public CompanyResponse getCompanyByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        return CompanyMapper.toResponse(product.getCompany());
    }

    @Override
    public Company getCompanyEntityById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }

    @Override
    public List<CompanyResponse> search(String query) {

        String q = (query == null) ? "" : query.trim();

        if (q.isEmpty()) {
            return List.of();
        }

        return companyRepository.search(q)
                .stream()
                .map(CompanyMapper::toResponse)
                .toList();
    }



    @Override
    public void uploadImage(Long companyId, MultipartFile file, CompanyImageType type) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        String fileName = fileStorageService.saveFile(file);

        switch (type) {
            case LOGO -> {
                if (company.getLogoUrl() != null) {
                    fileStorageService.deleteFile(company.getLogoUrl());
                }
                company.setLogoUrl(fileName);
            }
            case BANNER -> {
                if (company.getBannerUrl() != null) {
                    fileStorageService.deleteFile(company.getBannerUrl());
                }
                company.setBannerUrl(fileName);
            }
        }
        companyRepository.save(company);
    }



    @Override
    public void deleteImage(Long companyId, CompanyImageType type) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        switch (type) {
            case LOGO -> {
                if (company.getLogoUrl() != null) {
                    fileStorageService.deleteFile(company.getLogoUrl());
                    company.setLogoUrl(null);
                }
            }

            case BANNER -> {
                if (company.getBannerUrl() != null) {
                    fileStorageService.deleteFile(company.getBannerUrl());
                    company.setBannerUrl(null);
                }
            }
        }

        companyRepository.save(company);
    }







    private Company findCompany(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }

}

