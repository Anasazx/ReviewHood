package tn.anasazx.tunirate.product.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.category.entity.Category;
import tn.anasazx.tunirate.enums.CompanyStatus;
import tn.anasazx.tunirate.enums.GlobalRole;
import tn.anasazx.tunirate.product.dto.*;
import tn.anasazx.tunirate.subcategory.entity.Subcategory;
import tn.anasazx.tunirate.product.entity.Product;
import tn.anasazx.tunirate.product.entity.ProductImage;
import tn.anasazx.tunirate.product.repository.ProductRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ProductRepository productRepository;

    public ProductResponse toResponse(Product product) {

        ProductImage mainImage = product.getImages().stream()
                .filter(ProductImage::isMain)
                .findFirst()
                .orElse(null);

        // fallback if no main image exists
        if (mainImage == null && !product.getImages().isEmpty()) {
            mainImage = product.getImages().getFirst();
        }

        String categoryName = Optional.ofNullable(product.getSubcategory())
                .map(Subcategory::getCategory)
                .map(Category::getName)
                .orElse(null);

        String subcategoryName = Optional.ofNullable(product.getSubcategory())
                .map(Subcategory::getName)
                .orElse(null);

        boolean companyIsVerified =
                product.getCompany().isVerified();

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                categoryName,
                subcategoryName,
                product.getCompany().getId(),
                product.getCompany().getName(),
                companyIsVerified,
                product.getCompany().getLogoUrl(),
                mainImage == null ? null : ProductImageMapper.toResponse(mainImage),
                product.getReviewsAvg(),
                product.getReviewCount(),
                product.getCreatedAt()
        );
    }

    public CompanyProductResponse toCompanyResponse(Product product) {

        ProductImage mainImage = product.getImages().stream()
                .filter(ProductImage::isMain)
                .findFirst()
                .orElse(null);

        // fallback if no main image exists
        if (mainImage == null && !product.getImages().isEmpty()) {
            mainImage = product.getImages().getFirst();
        }

        String categoryName = Optional.ofNullable(product.getSubcategory())
                .map(Subcategory::getCategory)
                .map(Category::getName)
                .orElse(null);
        String subcategoryName = Optional.ofNullable(product.getSubcategory())
                .map(Subcategory::getName)
                .orElse(null);

        String createdByName = product.getCreatedBy().getName();

        if (product.getCreatedBy().getGlobalRole() == GlobalRole.ADMIN) {
            createdByName = "ReviewHood";
        }


        String updatedByName = null;

        if (product.getUpdatedBy() != null){
            if (product.getUpdatedBy().getGlobalRole() == GlobalRole.ADMIN) {
                updatedByName = "ReviewHood";
            }
            else {
                updatedByName = product.getUpdatedBy().getName();
            }
        }


        boolean companyIsVerified =  product.getCompany().isVerified();


        return new CompanyProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                categoryName,
                subcategoryName,
                product.getCompany().getId(),
                product.getCompany().getName(),
                companyIsVerified,
                product.getCompany().getLogoUrl(),
                mainImage == null ? null : ProductImageMapper.toResponse(mainImage),
                product.getCreatedAt(),
                createdByName,
                updatedByName,
                product.getStatus()
        );

    }

    public AdminProductResponse toAdminResponse(Product product) {

        ProductImage mainImage = product.getImages().stream()
                .filter(ProductImage::isMain)
                .findFirst()
                .orElse(null);

        // fallback if no main image exists
        if (mainImage == null && !product.getImages().isEmpty()) {
            mainImage = product.getImages().getFirst();
        }

        String categoryName = Optional.ofNullable(product.getSubcategory())
                .map(Subcategory::getCategory)
                .map(Category::getName)
                .orElse(null);

        String subcategoryName = Optional.ofNullable(product.getSubcategory())
                .map(Subcategory::getName)
                .orElse(null);

        Long subcategoryId = Optional.ofNullable(product.getSubcategory())
                .map(Subcategory::getId)
                .orElse(null);

        String updatedByName = null;
        Long updatedById = null;

        if (product.getUpdatedBy() != null) {
            updatedByName = product.getUpdatedBy().getName();
            updatedById = product.getUpdatedBy().getId();
        }


        boolean companyIsVerified =  product.getCompany().isVerified();



        return new AdminProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                categoryName,
                subcategoryId,
                subcategoryName,
                product.getCompany().getId(),
                product.getCompany().getName(),
                companyIsVerified,
                product.getCompany().getLogoUrl(),
                mainImage == null ? null : ProductImageMapper.toResponse(mainImage),
                product.getCreatedAt(),
                product.getCreatedBy().getName(),
                product.getCreatedBy().getId(),
                updatedByName,
                updatedById,
                product.getStatus()
        );

    }

    public ProductDetailsResponse toDetailsResponse(Product product) {

        List<ProductImageResponse> images = product.getImages()
                .stream()
                .map(ProductImageMapper::toResponse)
                .toList();


        String categoryName = Optional.ofNullable(product.getSubcategory())
                .map(Subcategory::getCategory)
                .map(Category::getName)
                .orElse(null);

        String subcategoryName = Optional.ofNullable(product.getSubcategory())
                .map(Subcategory::getName)
                .orElse(null);

        boolean companyIsVerified =  product.getCompany().isVerified();

        return new ProductDetailsResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                categoryName,
                subcategoryName,
                product.getCompany().getId(),
                product.getCompany().getName(),
                companyIsVerified,
                product.getCompany().getLogoUrl(),
                product.getReviewsAvg(),
                product.getReviewCount(),
                images,
                Optional.ofNullable(product.getSubcategory()).map(Subcategory::getCategory).map(Category::getId).orElse(null),
                Optional.ofNullable(product.getSubcategory()).map(Subcategory::getId).orElse(null),
                ratingDistribution(product.getId())
        );
    }

    private Map<Integer, Long> ratingDistribution(Long productId) {
        Map<Integer, Long> distribution = new LinkedHashMap<>();
        for (int stars = 5; stars >= 1; stars--) {
            distribution.put(stars, 0L);
        }
        for (Object[] row : productRepository.countReviewsByRating(productId)) {
            distribution.put((Integer) row[0], (Long) row[1]);
        }
        return distribution;
    }

    public AdminProductDetailsResponse toAdminDetailsResponse(Product product) {

        List<ProductImageResponse> images = product.getImages()
                .stream()
                .map(ProductImageMapper::toResponse)
                .toList();


        String categoryName = Optional.ofNullable(product.getSubcategory())
                .map(Subcategory::getCategory)
                .map(Category::getName)
                .orElse(null);

        String subcategoryName = Optional.ofNullable(product.getSubcategory())
                .map(Subcategory::getName)
                .orElse(null);

        Long subcategoryId = Optional.ofNullable(product.getSubcategory())
                .map(Subcategory::getId)
                .orElse(null);

        String updatedByName = null;
        Long updatedById = null;
        if (product.getUpdatedBy() != null) {
            updatedByName = product.getUpdatedBy().getName();
            updatedById = product.getUpdatedBy().getId();
        }

        boolean companyIsVerified =  product.getCompany().isVerified();


        return new AdminProductDetailsResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                categoryName,
                subcategoryId,
                subcategoryName,
                product.getCompany().getId(),
                product.getCompany().getName(),
                companyIsVerified,
                product.getCompany().getLogoUrl(),
                product.getReviewsAvg(),
                product.getReviewCount(),
                images,
                product.getCreatedBy().getName(),
                product.getCreatedBy().getId(),
                updatedByName,
                updatedById,
                product.getStatus().name()
        );
    }

    public ProductAIResponse toProductAIResponse(Product product){
        return new ProductAIResponse(product.getId(), product.getName(), product.getDescription());
    }

}
