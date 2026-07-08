package tn.anasazx.tunirate.company.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.anasazx.tunirate.company.dto.AdminCompanyRequest;
import tn.anasazx.tunirate.company.dto.AdminCompanyResponse;
import tn.anasazx.tunirate.company.dto.CompanyDetailResponse;
import tn.anasazx.tunirate.company.dto.CompanyResponse;
import tn.anasazx.tunirate.company.service.CompanyService;
import tn.anasazx.tunirate.enums.CompanyImageType;

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

    @GetMapping("/details/{companyId}")
    public ResponseEntity<CompanyDetailResponse> getCompanyDetailsById(@PathVariable Long companyId) {
        return ResponseEntity.ok(companyService.getCompanyDetailsById(companyId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/my")
    public ResponseEntity<CompanyResponse> getMyCompany() {
        return ResponseEntity.ok(companyService.getMyCompany());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-name")
    public ResponseEntity<CompanyResponse> getCompanyByName(@RequestParam String companyName) {
        return ResponseEntity.ok(companyService.getCompanyByName(companyName));
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

    //Admin method
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CompanyResponse> createCompanyAsAdmin(@Valid @RequestBody AdminCompanyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.createCompanyAsAdmin(request));
    }

    //Admin method
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> updateCompanyAsAdmin(@PathVariable Long companyId, @Valid @RequestBody AdminCompanyRequest request) {
        return ResponseEntity.ok(companyService.updateCompanyAsAdmin(companyId, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{companyId}/archive")
    public ResponseEntity<Void> archiveCompanyAsAdmin(@PathVariable Long companyId) {
        companyService.archiveCompanyAsAdmin(companyId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-product/{productId}")
    public ResponseEntity<CompanyResponse> getCompanyByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(companyService.getCompanyByProductId(productId));
    }

    //IMAGE CONTROLLER
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/logo")
    public ResponseEntity<Void> uploadLogo(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        companyService.uploadImage(id, file, CompanyImageType.LOGO);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/banner")
    public ResponseEntity<Void> uploadBanner(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        companyService.uploadImage(id, file, CompanyImageType.BANNER);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/logo")
    public ResponseEntity<Void> deleteLogo(@PathVariable Long id) {
        companyService.deleteImage(id, CompanyImageType.LOGO);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/banner")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        companyService.deleteImage(id, CompanyImageType.BANNER);
        return ResponseEntity.noContent().build();
    }

}
