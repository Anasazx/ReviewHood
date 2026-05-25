package tn.anasazx.tunirate.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.product.dto.ProductDetailsResponse;
import tn.anasazx.tunirate.product.dto.ProductRequest;
import tn.anasazx.tunirate.product.dto.ProductResponse;
import tn.anasazx.tunirate.product.entity.Product;
import tn.anasazx.tunirate.product.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


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
		return ResponseEntity.ok(productService.getProductDetails(id));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
		Product product = findProduct(id);
		Company company = findCompany(request.companyId());

		product.setName(request.name());
		product.setDescription(request.description());
		product.setCategory(request.category());
		product.setCompany(company);

		return ResponseEntity.ok(productService.updateProduct(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}

	private Product findProduct(Long id) {
        return productService.findProduct(id);
    }

	private Company findCompany(Long id) {
        return productService.findCompany(id);
    }


}
