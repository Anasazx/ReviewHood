package tn.anasazx.tunirate.company.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.anasazx.tunirate.company.dto.*;
import tn.anasazx.tunirate.company.service.CompanyService;
import tn.anasazx.tunirate.security.SecurityUtils;

import java.util.List;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long companyId) {
        return ResponseEntity.ok(companyService.getCompanyById(companyId));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<CompanyResponse>> getAllCompanies(Pageable pageable) {
        return ResponseEntity.ok(companyService.getAllCompanies(pageable));
    }

    @GetMapping("/details/{companyId}")
    public ResponseEntity<CompanyDetailResponse> getCompanyDetailsById(@PathVariable Long companyId) {
        return ResponseEntity.ok(companyService.getCompanyDetailsById(companyId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/my")
    public ResponseEntity<CompanyResponse> getMyCompany() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(companyService.getMyCompany(currentUserId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-name")
    public ResponseEntity<CompanyResponse> getCompanyByName(@RequestParam String companyName) {
        return ResponseEntity.ok(companyService.getCompanyByName(companyName));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-product/{productId}")
    public ResponseEntity<CompanyResponse> getCompanyByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(companyService.getCompanyByProductId(productId));
    }

    //Admin method
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompaniesAsAdmin() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    //Admin method
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/op/details/{companyId}")
    public ResponseEntity<AdminCompanyResponse> getCompanyDetailsByIdAsAdmin(@PathVariable Long companyId) {
        return ResponseEntity.ok(companyService.getCompanyDetailsByIdAsAdmin(companyId));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CompanyResponse> createCompanyAsAdmin(
            @RequestPart("data") AdminCompanyRequest request,
            @RequestPart(value = "logo", required = false) MultipartFile logo,
            @RequestPart(value = "banner", required = false) MultipartFile banner
    ) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(companyService.createCompanyAsAdmin(request, logo, banner, currentUserId));
    }

    //Admin method
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{companyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CompanyResponse> updateCompanyAsAdmin(
            @PathVariable Long companyId,
            @RequestPart("data") AdminCompanyRequest request,
            @RequestPart(value = "logo", required = false) MultipartFile logo,
            @RequestPart(value = "banner", required = false) MultipartFile banner
    ) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(
                companyService.updateCompanyAsAdmin(companyId, request, logo, banner, currentUserId)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{companyId}/status")
    public ResponseEntity<Void> updateCompanyStatusAsAdmin(@PathVariable Long companyId, @RequestBody CompanyStatusRequest request) {
        companyService.updateCompanyStatusAsAdmin(companyId, request.status());
        return ResponseEntity.noContent().build();
    }

}