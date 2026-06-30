package tn.anasazx.tunirate.company.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.anasazx.tunirate.company.dto.AdminCompanyResponse;
import tn.anasazx.tunirate.company.dto.CompanyRequest;
import tn.anasazx.tunirate.company.dto.CompanyResponse;
import tn.anasazx.tunirate.company.service.CompanyService;
import tn.anasazx.tunirate.enums.CompanyImageType;

import java.util.List;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long companyId) {
        return ResponseEntity.ok(companyService.getCompanyById(companyId));
    }

    @GetMapping("/my")
    public ResponseEntity<CompanyResponse> getMyCompany() {
        return ResponseEntity.ok(companyService.getMyCompany());
    }

    @GetMapping("/by-name")
    public ResponseEntity<CompanyResponse> getCompanyByName(@RequestParam String companyName) {
        return ResponseEntity.ok(companyService.getCompanyByName(companyName));
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }


    //Admin method
    @GetMapping("/details/{companyId}")
    public ResponseEntity<AdminCompanyResponse> getCompanyDetailsByIdAsAdmin(@PathVariable Long companyId) {
        return ResponseEntity.ok(companyService.getCompanyDetailsByIdAsAdmin(companyId));
    }

    //Admin method
    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.createCompany(request));
    }

    //Admin method
    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> updateCompany(@PathVariable Long companyId, @Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.ok(companyService.updateCompany(companyId, request));
    }

    //Admin method
    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long companyId) {
        companyService.deleteCompany(companyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-product/{productId}")
    public ResponseEntity<CompanyResponse> getCompanyByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(companyService.getCompanyByProductId(productId));
    }


    //IMAGE CONTROLLER

    @PostMapping("/{id}/logo")
    public ResponseEntity<Void> uploadLogo(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        companyService.uploadImage(id, file, CompanyImageType.LOGO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/banner")
    public ResponseEntity<Void> uploadBanner(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        companyService.uploadImage(id, file, CompanyImageType.BANNER);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/logo")
    public ResponseEntity<Void> deleteLogo(@PathVariable Long id) {
        companyService.deleteImage(id, CompanyImageType.LOGO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/banner")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        companyService.deleteImage(id, CompanyImageType.BANNER);
        return ResponseEntity.noContent().build();
    }




}
