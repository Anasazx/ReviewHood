package tn.anasazx.tunirate.product.service;

import tn.anasazx.tunirate.product.dto.*;

import java.util.List;

public interface ProductService {

    //Public methods
    ProductDetailsResponse getProductDetailsById(Long productId);
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(Long productId);
    List<ProductResponse> getProductsByCompanyId(Long companyId);

    //Admin methods
    AdminProductDetailsResponse getProductDetailsByIdAsAdmin(Long productId);
    List<AdminProductResponse> getProductsAsAdmin();
    AdminProductResponse createProductAsAdmin(AdminProductRequest request);
    AdminProductResponse updateProductAsAdmin(Long id, AdminProductRequest request);
    void deleteProductAsAdmin(Long productId);

    //Company methods
    List<CompanyProductResponse> getProductsAsCompany();
    CompanyProductResponse createProductAsCompany(CompanyProductRequest request);
    CompanyProductResponse getProductByIdAsCompany(Long productId);


}
