package tn.anasazx.tunirate.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.anasazx.tunirate.enums.ProductStatus;
import tn.anasazx.tunirate.product.dto.*;
import tn.anasazx.tunirate.product.service.ProductService;
import tn.anasazx.tunirate.security.SecurityUtils;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

	@GetMapping
	public ResponseEntity<Page<ProductResponse>> getAllProducts(
			Pageable pageable,
			@RequestParam(required = false) Long categoryId,
			@RequestParam(required = false) Long subcategoryId
	) {
		return ResponseEntity.ok(
				productService.getAllProducts(categoryId, subcategoryId, pageable)
		);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/uncategorized")
	public ResponseEntity<Page<ProductAIResponse>> getUncategorizedProducts(Pageable pageable) {
		return ResponseEntity.ok(
				productService.getUncategorizedProducts(pageable)
		);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/{productId}/subcategory")
	public ResponseEntity<Void> updateProductSubcategory(@PathVariable Long productId, @RequestBody UpdateSubcategoryRequest request) {
		productService.updateSubcategory(productId, request.subcategoryId());
		return ResponseEntity.ok().build();
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
	public ResponseEntity<Page<ProductResponse>> getProductsByCompanyId(
			@PathVariable Long companyId,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) Long subcategoryId,
			Pageable pageable) {
		return ResponseEntity.ok(productService.getProductsByCompanyId(companyId, name, subcategoryId, pageable));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/op/{productId}/details")
	public ResponseEntity<AdminProductDetailsResponse> getProductDetailsAsAdmin(@PathVariable Long productId){
		return ResponseEntity.ok(productService.getProductDetailsByIdAsAdmin(productId));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/op")
	public ResponseEntity<Page<AdminProductResponse>> getProductsAsAdmin(
			@RequestParam(required = false) String q,
			@RequestParam(required = false) ProductStatus status,
			@RequestParam(required = false) Long companyId,
			@RequestParam(required = false) Long subcategoryId,
			@RequestParam(required = false) Boolean uncategorized,
			@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return ResponseEntity.ok(productService.getProductsAsAdmin(q, status, companyId, subcategoryId, uncategorized, pageable));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/op/status-counts")
	public ResponseEntity<Map<ProductStatus, Long>> countProductsByStatus(@RequestParam(required = false) Long companyId) {
		return ResponseEntity.ok(productService.countProductsByStatus(companyId));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<AdminProductResponse> createProductAsAdmin(@RequestPart("data") @Valid AdminProductRequest request, @RequestPart(value = "images", required = false) List<MultipartFile> images) {
		Long currentUserId = SecurityUtils.getCurrentUserId();
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(productService.createProductAsAdmin(request, images, currentUserId));
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
	@PreAuthorize("@companyAccess.isMember()")
	@PostMapping("/c")
	public ResponseEntity<CompanyProductResponse> createProductAsCompany(@Valid @RequestBody CompanyProductRequest request) {
		Long currentUserId = SecurityUtils.getCurrentUserId();
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProductAsCompany(request, currentUserId));
	}

	@PreAuthorize("@companyAccess.isMember()")
	@PutMapping("/c/{productId}")
	public ResponseEntity<CompanyProductResponse> updateProductAsCompany(@PathVariable Long productId, @Valid @RequestBody CompanyProductRequest request) {
		Long currentUserId = SecurityUtils.getCurrentUserId();
		return ResponseEntity.ok(productService.updateProductAsCompany(productId, request, currentUserId));
	}

	// The current user's company products, paginated (drafts included)
	@PreAuthorize("@companyAccess.isMember()")
	@GetMapping("/my")
	public ResponseEntity<Page<CompanyProductResponse>> getProductsAsCompany(
			@RequestParam(required = false) String q,
			@RequestParam(required = false) ProductStatus status,
			@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		Long currentUserId = SecurityUtils.getCurrentUserId();
		return ResponseEntity.ok(productService.getProductsAsCompany(currentUserId, q, status, pageable));
	}

	@PreAuthorize("@companyAccess.isMember()")
	@GetMapping("/my/status-counts")
	public ResponseEntity<Map<ProductStatus, Long>> countProductsByStatusAsCompany() {
		Long currentUserId = SecurityUtils.getCurrentUserId();
		return ResponseEntity.ok(productService.countProductsByStatusAsCompany(currentUserId));
	}

	@PreAuthorize("@companyAccess.isMember()")
	@GetMapping("/c/{productId}")
	public ResponseEntity<AdminProductDetailsResponse> getProductByIdAsCompany(@PathVariable Long productId) {
		Long currentUserId = SecurityUtils.getCurrentUserId();
		return ResponseEntity.ok(productService.getProductByIdAsCompany(productId, currentUserId));
	}





}
