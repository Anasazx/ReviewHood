package tn.anasazx.tunirate.product.service;

import tn.anasazx.tunirate.product.dto.*;

import java.util.List;

public interface ProductService {

    //Public methods
    ProductDetailsResponse getProductDetailsById(Long id);
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(Long id);
    List<ProductResponse> getProductsByCompanyId(Long companyId);

    //Admin methods
    List<AdminProductResponse> getProductsAsAdmin();
    AdminProductResponse createProductAsAdmin(AdminProductRequest request);
    AdminProductResponse updateProductAsAdmin(Long id, AdminProductRequest request);
    void deleteProductAsAdmin(Long id);

    //Company methods
    List<CompanyProductResponse> getProductsAsCompany();
    CompanyProductResponse createProductAsCompany(CompanyProductRequest request);

}
