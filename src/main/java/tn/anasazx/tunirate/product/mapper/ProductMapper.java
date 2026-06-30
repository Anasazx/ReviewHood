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

    public static ProductResponse toResponse(Product product) {

        Optional<String> imageUrl = product.getImages().stream()
                .filter(ProductImage::isMain)
                .map(ProductImage::getUrl)
                .findFirst()
                .or(() -> product.getImages().stream()
                        .map(ProductImage::getUrl)
                        .findFirst());
        String categoryName = Optional.ofNullable(product.getSubcategory())
                .map(Subcategory::getCategory)
                .map(Category::getName)
                .orElse(null);
        String subcategoryName = Optional.ofNullable(product.getSubcategory())
                .map(Subcategory::getName)
                .orElse(null);

        boolean companyIsVerified =  product.getCompany().getStatus() == CompanyStatus.ACTIVE;

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
                imageUrl.orElse(null),
                product.getCreatedAt()
        );

    }

    public static CompanyProductResponse toCompanyResponse(Product product) {

        Optional<String> imageUrl = product.getImages().stream()
                .filter(ProductImage::isMain)
                .map(ProductImage::getUrl)
                .findFirst()
                .or(() -> product.getImages().stream()
                        .map(ProductImage::getUrl)
                        .findFirst());
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
                imageUrl.orElse(null),
                product.getCreatedAt(),
                createdByName,
                updatedByName,
                product.getStatus()
        );

    }

    public static AdminProductResponse toAdminResponse(Product product) {

        Optional<String> imageUrl = product.getImages().stream()
                .filter(ProductImage::isMain)
                .map(ProductImage::getUrl)
                .findFirst()
                .or(() -> product.getImages().stream()
                        .map(ProductImage::getUrl)
                        .findFirst());
        String categoryName = Optional.ofNullable(product.getSubcategory())
                .map(Subcategory::getCategory)
                .map(Category::getName)
                .orElse(null);
        String subcategoryName = Optional.ofNullable(product.getSubcategory())
                .map(Subcategory::getName)
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
                subcategoryName,
                product.getCompany().getId(),
                product.getCompany().getName(),
                companyIsVerified,
                product.getCompany().getLogoUrl(),
                imageUrl.orElse(null),
                product.getCreatedAt(),
                product.getCreatedBy().getName(),
                product.getCreatedBy().getId(),
                updatedByName,
                updatedById,
                product.getStatus()
        );

    }

    public static ProductDetailsResponse toDetailsResponse(Product product, double avgRating, long reviewsCount) {

        List<ProductImageResponse> images = product.getImages()
                .stream()
                .map(img -> new ProductImageResponse(
                        img.getId(),
                        img.getUrl(),
                        img.isMain()
                ))
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

    public static AdminProductDetailsResponse toAdminDetailsResponse(Product product, double avgRating, long reviewsCount) {

        List<ProductImageResponse> images = product.getImages()
                .stream()
                .map(img -> new ProductImageResponse(
                        img.getId(),
                        img.getUrl(),
                        img.isMain()
                ))
                .toList();


        String categoryName = Optional.ofNullable(product.getSubcategory())
                .map(Subcategory::getCategory)
                .map(Category::getName)
                .orElse(null);

        String subcategoryName = Optional.ofNullable(product.getSubcategory())
                .map(Subcategory::getName)
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
