package tn.anasazx.tunirate.company.service.implementation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.companySocialLink.entity.CompanySocialLink;
import tn.anasazx.tunirate.companySocialLink.mapper.CompanySocialLinkMapper;
import tn.anasazx.tunirate.company.dto.AdminCompanyRequest;
import tn.anasazx.tunirate.company.dto.AdminCompanyResponse;
import tn.anasazx.tunirate.company.dto.CompanyDetailResponse;
import tn.anasazx.tunirate.company.dto.CompanyResponse;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.company.mapper.CompanyMapper;
import tn.anasazx.tunirate.company.repository.CompanyRepository;
import tn.anasazx.tunirate.company.service.CompanyService;
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
    private final CompanyMapper companyMapper;


    @Override
    public CompanyResponse getCompanyById(Long id) {
        return companyMapper.toResponse(findCompany(id));
    }

    @Override
    public CompanyDetailResponse getCompanyDetailsById(Long id) {
        return companyMapper.toDetailResponse(findCompany(id));
    }

    @Override
    public CompanyResponse getMyCompany() {
        long currentUserId = SecurityUtils.getCurrentUserId();
        CompanyMember membership = companyMemberRepository.findFirstByUserId(currentUserId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User doesn't belong to any company!")
        );

        //Extract company entity from the membership response
        Company company = membership.getCompany();

        return companyMapper.toResponse(company);
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
    public AdminCompanyResponse getCompanyDetailsByIdAsAdmin(Long companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found")
        );
        return companyMapper.toAdminResponse(company);
    }

    @Override
    @Transactional
    public CompanyResponse createCompanyAsAdmin(AdminCompanyRequest request, MultipartFile logo, MultipartFile banner) {

        companyRepository.findByNameIgnoreCase(request.name())
                .ifPresent(existing -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Company name already exists"
                    );
                });

        Long currentUserId = SecurityUtils.getCurrentUserId();
        User currentUser = findUser(currentUserId);

        Company company = new Company();

        company.setName(request.name());
        company.setDescription(request.description());
        company.setPhoneNumber(request.phoneNumber());
        company.setAddress(request.address());
        company.setCountry(request.country());
        company.setIndustry(request.industry());

        company.setCreatedBy(currentUser);
        company.setStatus(
                request.status() != null
                        ? request.status()
                        : CompanyStatus.PENDING
        );

        if (logo != null && !logo.isEmpty()) {
            company.setLogoUrl(fileStorageService.saveFile(logo));
        }

        if (banner != null && !banner.isEmpty()) {
            company.setBannerUrl(fileStorageService.saveFile(banner));
        }

        companyRepository.save(company);

        return companyMapper.toResponse(company);
    }

    @Override
    @Transactional
    public CompanyResponse updateCompanyAsAdmin(Long companyId, AdminCompanyRequest request, MultipartFile logo, MultipartFile banner) {

        Company company = findCompany(companyId);

        // Prevent duplicate names
        companyRepository.findByNameIgnoreCase(request.name())
                .filter(existing -> !existing.getId().equals(companyId))
                .ifPresent(existing -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Company name already exists"
                    );
                });

        Long currentUserId = SecurityUtils.getCurrentUserId();
        User currentUser = findUser(currentUserId);

        // Basic fields
        company.setName(request.name());
        company.setDescription(request.description());
        company.setPhoneNumber(request.phoneNumber());
        company.setAddress(request.address());
        company.setCountry(request.country());
        company.setIndustry(request.industry());
        company.setUpdatedBy(currentUser);


        // Social links
        if (request.socialLinks() != null) {

            company.clearSocialLinks();

            request.socialLinks().forEach(req -> {

                CompanySocialLink link =
                        CompanySocialLinkMapper.toEntity(req);

                company.addSocialLink(link);

            });
        }



        // Status validation
        if (request.status() == CompanyStatus.ARCHIVED
                && !company.getProducts().isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Company has products"
            );
        }

        company.setStatus(request.status());



        // Logo replacement
        if (logo != null && !logo.isEmpty()) {

            if (company.getLogoUrl() != null) {
                fileStorageService.deleteFile(company.getLogoUrl());
            }

            company.setLogoUrl(
                    fileStorageService.saveFile(logo)
            );
        }



        // Banner replacement
        if (banner != null && !banner.isEmpty()) {

            if (company.getBannerUrl() != null) {
                fileStorageService.deleteFile(company.getBannerUrl());
            }

            company.setBannerUrl(
                    fileStorageService.saveFile(banner)
            );
        }



        return companyMapper.toResponse(
                companyRepository.save(company)
        );
    }

    @Override
    @Transactional
    public void updateCompanyStatusAsAdmin(Long companyId, CompanyStatus status) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        if (status == CompanyStatus.ARCHIVED && !company.getProducts().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot archive company with products");
        }
        company.setStatus(status);
        companyRepository.save(company);
    }

    @Override
    public CompanyResponse getCompanyByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return companyMapper.toResponse(product.getCompany());
    }

    @Override
    public List<CompanyResponse> search(String query) {

        String q = (query == null) ? "" : query.trim();

        if (q.isEmpty()) {
            return List.of();
        }

        return companyRepository.search(q)
                .stream()
                .map(companyMapper::toResponse)
                .toList();
    }

    // Method helpers
    private Company findCompany(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }

    private User findUser(Long userId){
        return userRepository.findFirstById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

}

