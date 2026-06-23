package tn.anasazx.tunirate.product.service;

import tn.anasazx.tunirate.subcategory.entity.Subcategory;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.product.dto.ProductDetailsResponse;
import tn.anasazx.tunirate.product.dto.ProductRequest;
import tn.anasazx.tunirate.product.dto.ProductResponse;
import tn.anasazx.tunirate.product.entity.Product;

import java.util.List;

public interface ProductService {
    ProductDetailsResponse getProductDetails(Long id);
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(Long id);
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
    Product findProduct(Long id);
    Company findCompany(Long id);
    Subcategory findSubcategory(Long id);
    List<ProductResponse> search(String query);
    List<ProductResponse> getProductsByCompanyId(Long companyId);
}
