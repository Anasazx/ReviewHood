package tn.anasazx.tunirate.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.anasazx.tunirate.product.dto.*;
import tn.anasazx.tunirate.product.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

	//public methods
	@GetMapping
	public ResponseEntity<Page<ProductResponse>> getAllProducts(Pageable pageable) {
		return ResponseEntity.ok(productService.getAllProducts(pageable));
	}

	//On the public front this is not used, maybe in the future, then I will remove the preAuthorize
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{productId}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {
		return ResponseEntity.ok(productService.getProductById(productId));
	}

	@GetMapping("/{productId}/details")
	public ResponseEntity<ProductDetailsResponse> getProductDetails(@PathVariable Long productId){
		return ResponseEntity.ok(productService.getProductDetailsById(productId));
	}

	@GetMapping("/company/{companyId}")
	public ResponseEntity<Page<ProductResponse>> getProductsByCompanyId(@PathVariable Long companyId, Pageable pageable) {
		return ResponseEntity.ok(productService.getProductsByCompanyId(companyId, pageable));
	}

	//Admin methods
	//Only the admin can use this
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/op")
	public ResponseEntity<List<AdminProductResponse>> getProductsAsAdmin() {
		return ResponseEntity.ok(productService.getProductsAsAdmin());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<AdminProductResponse> createProductAsAdmin(@Valid @RequestBody AdminProductRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProductAsAdmin(request));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<AdminProductResponse> updateProductAsAdmin(@PathVariable Long id, @Valid @RequestBody AdminProductRequest request) {
		return ResponseEntity.ok(productService.updateProductAsAdmin(id, request));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/op/{productId}/archive")
	public ResponseEntity<Void> archiveProductAsAdmin(@PathVariable Long productId) {
		productService.archiveProductAsAdmin(productId);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/op/{productId}/details")
	public ResponseEntity<AdminProductDetailsResponse> getProductDetailsAsAdmin(@PathVariable Long productId){
		return ResponseEntity.ok(productService.getProductDetailsByIdAsAdmin(productId));
	}


	//Company methods
	//This is for companies
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/c")
	public ResponseEntity<CompanyProductResponse> createProductAsCompany(@Valid @RequestBody CompanyProductRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProductAsCompany(request));
	}


	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/my")
	public ResponseEntity<List<CompanyProductResponse>> getProductsAsCompany() {
		return ResponseEntity.ok(productService.getProductsAsCompany());
	}


	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/c/{productId}")
	public ResponseEntity<CompanyProductResponse> getProductByIdAsCompany(@PathVariable Long productId) {
		return ResponseEntity.ok(productService.getProductByIdAsCompany(productId));
	}





}
