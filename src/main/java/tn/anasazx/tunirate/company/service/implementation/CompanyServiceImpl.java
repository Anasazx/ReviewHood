package tn.anasazx.tunirate.company.service.implementation;

import java.util.Optional;
import tn.anasazx.tunirate.enums.ProductStatus;
import tn.anasazx.tunirate.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.companySocialLink.entity.CompanySocialLink;
import tn.anasazx.tunirate.companySocialLink.mapper.CompanySocialLinkMapper;
import tn.anasazx.tunirate.company.dto.AdminCompanyRequest;
import tn.anasazx.tunirate.company.dto.AdminCompanyResponse;
import tn.anasazx.tunirate.enums.CompanyRole;
import tn.anasazx.tunirate.company.dto.CompanyProfileRequest;
import tn.anasazx.tunirate.company.dto.AdminCompanySummaryResponse;
import tn.anasazx.tunirate.company.repository.CompanySpecifications;
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
import tn.anasazx.tunirate.subcategory.dto.SubcategoryResponse;
import tn.anasazx.tunirate.subcategory.mapper.SubcategoryMapper;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final CompanyMemberRepository companyMemberRepository;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;
    private final CompanyMapper companyMapper;
    private final SubcategoryMapper subcategoryMapper;


    @Override
    public CompanyResponse getCompanyById(Long id) {
        return companyMapper.toResponse(findCompany(id));
    }

    @Override
    public CompanyDetailResponse getCompanyDetailsById(Long id) {
        // Public page: suspended companies are hidden
        Company company = companyRepository.findByIdAndStatusNot(id, CompanyStatus.SUSPENDED)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        return toDetailResponse(company);
    }

    private CompanyDetailResponse toDetailResponse(Company company) {
        Long id = company.getId();
        List<SubcategoryResponse> subcategories = productRepository.findDistinctSubcategoriesByCompanyId(id).stream()
                .map(subcategoryMapper::toResponse)
                .toList();

        return companyMapper.toDetailResponse(
                company,
                subcategories,
                Optional.ofNullable(reviewRepository.findAverageRatingByCompanyId(id)).orElse(0.0),
                reviewRepository.countByProductCompanyId(id),
                productRepository.countByCompanyIdAndStatus(id, ProductStatus.PUBLISHED)
        );
    }

    @Override
    public CompanyDetailResponse getMyCompany(Long currentUserId) {
        return toDetailResponse(findMyMembership(currentUserId).getCompany());
    }

    @Override
    @Transactional
    public CompanyDetailResponse updateMyCompany(CompanyProfileRequest request, MultipartFile logo, MultipartFile banner, Long currentUserId) {
        CompanyMember membership = findMyMembership(currentUserId);
        if (membership.getCompanyRole() != CompanyRole.HEAD) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the company head can edit the company profile");
        }

        Company company = membership.getCompany();
        company.setDescription(request.description());
        company.setPhoneNumber(request.phoneNumber());
        company.setAddress(request.address());
        company.setCountry(request.country());
        company.setIndustry(request.industry());
        company.setUpdatedBy(membership.getUser());

        if (request.socialLinks() != null) {
            company.clearSocialLinks();
            request.socialLinks().forEach(req -> company.addSocialLink(CompanySocialLinkMapper.toEntity(req)));
        }

        if (logo != null && !logo.isEmpty()) {
            String old = company.getLogoUrl();
            company.setLogoUrl(fileStorageService.saveFile(logo));
            if (old != null) fileStorageService.deleteFile(old);
        }
        if (banner != null && !banner.isEmpty()) {
            String old = company.getBannerUrl();
            company.setBannerUrl(fileStorageService.saveFile(banner));
            if (old != null) fileStorageService.deleteFile(old);
        }

        companyRepository.save(company);
        return toDetailResponse(company);
    }

    private CompanyMember findMyMembership(Long currentUserId) {
        return companyMemberRepository.findFirstByUserId(currentUserId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't belong to any company")
        );
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
        return companyMapper.toAdminResponse(company, productRepository.countByCompanyId(companyId));
    }

    @Override
    public Page<AdminCompanySummaryResponse> getCompaniesAsAdmin(String q, CompanyStatus status, Pageable pageable) {
        Specification<Company> spec = Specification
                .where(CompanySpecifications.hasNameLike(q))
                .and(CompanySpecifications.hasStatus(status));

        return companyRepository.findAll(spec, pageable)
                .map(company -> companyMapper.toAdminSummaryResponse(
                        company,
                        productRepository.countByCompanyId(company.getId()),
                        companyMemberRepository.countByCompanyId(company.getId())
                ));
    }

    @Override
    public Map<CompanyStatus, Long> countCompaniesByStatus() {
        Map<CompanyStatus, Long> counts = new EnumMap<>(CompanyStatus.class);
        for (CompanyStatus status : CompanyStatus.values()) {
            counts.put(status, 0L);
        }
        for (Object[] row : companyRepository.countByStatusGrouped()) {
            counts.put((CompanyStatus) row[0], (Long) row[1]);
        }
        return counts;
    }

    @Override
    @Transactional
    public CompanyResponse createCompanyAsAdmin(AdminCompanyRequest request, MultipartFile logo, MultipartFile banner, Long currentUserId) {

        companyRepository.findByNameIgnoreCase(request.name())
                .ifPresent(existing -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Company name already exists"
                    );
                });

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

        if (request.socialLinks() != null) {
            request.socialLinks().forEach(req -> company.addSocialLink(CompanySocialLinkMapper.toEntity(req)));
        }

        companyRepository.save(company);

        return companyMapper.toResponse(company);
    }

    @Override
    @Transactional
    public CompanyResponse updateCompanyAsAdmin(Long companyId, AdminCompanyRequest request, MultipartFile logo, MultipartFile banner, Long currentUserId) {

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
                && productRepository.countByCompanyId(companyId) > 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Company has products"
            );
        }

        if (request.status() != null) {
            applyStatus(company, request.status(), currentUser);
        }



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
        if (status == CompanyStatus.ARCHIVED && productRepository.countByCompanyId(companyId) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot archive company with products");
        }
        applyStatus(company, status, findUser(SecurityUtils.getCurrentUserId()));
        companyRepository.save(company);
    }

    @Override
    @Transactional
    public void setVerified(Long companyId, boolean verified, Long adminId) {
        Company company = findCompany(companyId);
        if (verified && company.getStatus() != CompanyStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only active companies can be verified");
        }
        company.setVerifiedBy(verified ? findUser(adminId) : null);
        company.setVerifiedAt(verified ? LocalDateTime.now() : null);
        companyRepository.save(company);
    }

    // Activating a company records who verified it and when
    private void applyStatus(Company company, CompanyStatus status, User admin) {
        if (status == CompanyStatus.ACTIVE && company.getStatus() != CompanyStatus.ACTIVE) {
            company.setVerifiedBy(admin);
            company.setVerifiedAt(LocalDateTime.now());
        }
        company.setStatus(status);
    }

    @Override
    public CompanyResponse getCompanyByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return companyMapper.toResponse(product.getCompany());
    }

    @Override
    public Page<CompanyResponse> getAllCompanies(Pageable pageable) {
        return companyRepository.findAllByStatusNot(CompanyStatus.SUSPENDED, pageable)
                .map(companyMapper::toResponse);
    }

    @Override
    public List<CompanyResponse> search(String query) {

        String q = (query == null) ? "" : query.trim();

        if (q.isEmpty()) {
            return List.of();
        }

        return companyRepository.search(q, CompanyStatus.SUSPENDED, PageRequest.of(0, 20))
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

