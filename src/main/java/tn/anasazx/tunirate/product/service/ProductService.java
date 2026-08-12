package tn.anasazx.tunirate.product.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import tn.anasazx.tunirate.enums.ProductStatus;
import tn.anasazx.tunirate.product.dto.*;

import java.util.List;

public interface ProductService {

    //Public methods
    ProductDetailsResponse getProductDetailsById(Long productId);

    Page<ProductAIResponse> getUncategorizedProducts(Pageable pageable);

    void updateSubcategory(Long productId, Long subcategoryId);

    Page<ProductResponse> getAllProducts(Long categoryId, Long subcategoryId, Pageable pageable);
    ProductResponse getProductById(Long productId);
    Page<ProductResponse> getProductsByCompanyId(Long companyId, String name, Long subcategoryId, Pageable pageable);

    //Admin methods
    AdminProductDetailsResponse getProductDetailsByIdAsAdmin(Long productId);
    List<AdminProductResponse> getProductsAsAdmin();
    AdminProductResponse createProductAsAdmin(AdminProductRequest request, List<MultipartFile> images, Long currentUserId);
    AdminProductResponse updateProductAsAdmin(Long id, AdminProductRequest request, List<MultipartFile> images);
    void updateProductStatusAsAdmin(Long productId, ProductStatus productStatus);


    //Company methods
    List<CompanyProductResponse> getProductsAsCompany(Long currentUserId);
    CompanyProductResponse createProductAsCompany(CompanyProductRequest request, Long currentUserId);
    CompanyProductResponse getProductByIdAsCompany(Long productId);




}
