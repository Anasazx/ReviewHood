package tn.anasazx.tunirate.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.anasazx.tunirate.product.dto.*;
import tn.anasazx.tunirate.product.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

	@PreAuthorize("hasRole('ADMIN')")
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

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/op/{productId}/details")
	public ResponseEntity<AdminProductDetailsResponse> getProductDetailsAsAdmin(@PathVariable Long productId){
		return ResponseEntity.ok(productService.getProductDetailsByIdAsAdmin(productId));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/op")
	public ResponseEntity<List<AdminProductResponse>> getProductsAsAdmin() {
		return ResponseEntity.ok(productService.getProductsAsAdmin());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<AdminProductResponse> createProductAsAdmin(@RequestPart("data") @Valid AdminProductRequest request, @RequestPart(value = "images", required = false) List<MultipartFile> images) {
		System.out.println("checkpoint one");
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(productService.createProductAsAdmin(request, images));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<AdminProductResponse> updateProductAsAdmin(@PathVariable Long id, @RequestPart("data") @Valid AdminProductRequest request, @RequestPart(value = "images", required = false) List<MultipartFile> images) {
		return ResponseEntity.ok(productService.updateProductAsAdmin(id, request, images));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/op/{productId}/status")
	public ResponseEntity<Void> updateProductStatusAsAdmin(@PathVariable Long productId, @RequestBody ProductStatusRequest request) {
		productService.updateProductStatusAsAdmin(productId, request.status());
		return ResponseEntity.noContent().build();
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
