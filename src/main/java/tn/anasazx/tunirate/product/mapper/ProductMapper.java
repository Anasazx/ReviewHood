package tn.anasazx.tunirate.product.mapper;

import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.category.entity.Category;
import tn.anasazx.tunirate.enums.CompanyStatus;
import tn.anasazx.tunirate.enums.GlobalRole;
import tn.anasazx.tunirate.product.dto.*;
import tn.anasazx.tunirate.subcategory.entity.Subcategory;
import tn.anasazx.tunirate.product.entity.Product;
import tn.anasazx.tunirate.product.entity.ProductImage;

import java.util.List;
import java.util.Optional;

@Component
public class ProductMapper {



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
                product.getCompany().getStatus() == CompanyStatus.ACTIVE;

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
            createdByName = "TunisiaRate Admin";
        }


        String updatedByName = null;

        if (product.getUpdatedBy() != null){
            if (product.getUpdatedBy().getGlobalRole() == GlobalRole.ADMIN) {
                updatedByName = "TunisiaRate Admin";
            }
            else {
                updatedByName = product.getUpdatedBy().getName();
            }
        }


        boolean companyIsVerified =  product.getCompany().getStatus() == CompanyStatus.ACTIVE;


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


        boolean companyIsVerified =  product.getCompany().getStatus() == CompanyStatus.ACTIVE;



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

    public ProductDetailsResponse toDetailsResponse(Product product, double avgRating, long reviewsCount) {

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

        boolean companyIsVerified =  product.getCompany().getStatus() == CompanyStatus.ACTIVE;

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
                avgRating,
                reviewsCount,
                images
        );
    }

    public AdminProductDetailsResponse toAdminDetailsResponse(Product product, double avgRating, long reviewsCount) {

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

        boolean companyIsVerified =  product.getCompany().getStatus() == CompanyStatus.ACTIVE;


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
                avgRating,
                reviewsCount,
                images,
                product.getCreatedBy().getName(),
                product.getCreatedBy().getId(),
                updatedByName,
                updatedById,
                product.getStatus().name()
        );
    }

}
