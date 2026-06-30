package tn.anasazx.tunirate.company.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.company.dto.AdminCompanyRequest;
import tn.anasazx.tunirate.company.dto.AdminCompanyResponse;
import tn.anasazx.tunirate.company.dto.CompanyResponse;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.company.mapper.CompanyMapper;
import tn.anasazx.tunirate.company.repository.CompanyRepository;
import tn.anasazx.tunirate.company.service.CompanyService;
import tn.anasazx.tunirate.enums.CompanyImageType;
import tn.anasazx.tunirate.enums.CompanyStatus;
import tn.anasazx.tunirate.fileStorage.service.FileStorageService;
import tn.anasazx.tunirate.membership.entity.CompanyMember;
import tn.anasazx.tunirate.membership.repository.CompanyMemberRepository;
import tn.anasazx.tunirate.product.entity.Product;
import tn.anasazx.tunirate.product.repository.ProductRepository;
import tn.anasazx.tunirate.security.SecurityUtils;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    private final CompanyMemberRepository companyMemberRepository;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;


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
    public CompanyResponse createCompanyAsAdmin(AdminCompanyRequest request) {

        companyRepository.findByNameIgnoreCase(request.name()).ifPresent(existing -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Company name already exists");
        });

        Long currentUserId = SecurityUtils.getCurrentUserId();

        User currentUser = findUser(currentUserId);

        Company company = new Company();
        company.setName(request.name());
        company.setDescription(request.description());
        company.setPhoneNumber(request.phoneNumber());
        company.setWebsiteUrl(request.websiteUrl());
        company.setAddress(request.address());
        company.setCountry(request.country());
        company.setIndustry(request.industry());
        company.setCreatedBy(currentUser);
        company.setStatus(request.status() != null ? request.status() : CompanyStatus.PENDING);


        return CompanyMapper.toResponse(companyRepository.save(company));
    }

    @Override
    public CompanyResponse updateCompanyAsAdmin(Long id, AdminCompanyRequest request) {
        Company company = findCompany(id);

        companyRepository.findByNameIgnoreCase(request.name())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Company name already exists");
                });

        Long currentUserId = SecurityUtils.getCurrentUserId();

        User currentUser = findUser(currentUserId);

        company.setName(request.name());
        company.setDescription(request.description());
        company.setPhoneNumber(request.phoneNumber());
        company.setWebsiteUrl(request.websiteUrl());
        company.setAddress(request.address());
        company.setCountry(request.country());
        company.setIndustry(request.industry());
        company.setUpdatedBy(currentUser);
        company.setStatus(request.status());

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

    private User findUser(Long userId){
        return userRepository.findFirstById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

}

