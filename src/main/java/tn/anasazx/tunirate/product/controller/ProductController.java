package tn.anasazx.tunirate.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.anasazx.tunirate.product.dto.*;
import tn.anasazx.tunirate.product.service.ProductService;
import tn.anasazx.tunirate.security.SecurityUtils;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;



	//public methods
	@GetMapping
	public ResponseEntity<List<ProductResponse>> getAllProducts() {
		return ResponseEntity.ok(productService.getAllProducts());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
		return ResponseEntity.ok(productService.getProductById(id));
	}


	@GetMapping("/{id}/details")
	public ResponseEntity<ProductDetailsResponse> getProductDetails(@PathVariable Long id){
		return ResponseEntity.ok(productService.getProductDetailsById(id));
	}

	@GetMapping("/company/{companyId}")
	public ResponseEntity<List<ProductResponse>> getProductsByCompanyId(@PathVariable Long companyId) {
		return ResponseEntity.ok(productService.getProductsByCompanyId(companyId));
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
	@DeleteMapping("/{id}")
	//TODO: future update; Remove this hard delete; and change it like (deleted = true);
	public ResponseEntity<Void> deleteProductAsAdmin(@PathVariable Long id) {
		productService.deleteProductAsAdmin(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/op/{id}/details")
	public ResponseEntity<AdminProductDetailsResponse> getProductDetailsAsAdmin(@PathVariable Long id){
		return ResponseEntity.ok(productService.getProductDetailsByIdAsAdmin(id));
	}


	//Company methods
	//This is for companies
	@PostMapping("/c")
	public ResponseEntity<CompanyProductResponse> createProductAsCompany(@Valid @RequestBody CompanyProductRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProductAsCompany(request));
	}

	@GetMapping("/my")
	public ResponseEntity<List<CompanyProductResponse>> getProductsAsCompany() {
		return ResponseEntity.ok(productService.getProductsAsCompany());
	}





}
