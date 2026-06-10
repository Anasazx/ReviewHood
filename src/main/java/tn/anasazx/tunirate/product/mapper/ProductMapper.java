package tn.anasazx.tunirate.product.mapper;

import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.categories.category.entity.Category;
import tn.anasazx.tunirate.categories.subcategory.entity.Subcategory;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.product.dto.ProductDetailsResponse;
import tn.anasazx.tunirate.product.dto.ProductImageResponse;
import tn.anasazx.tunirate.product.dto.ProductRequest;
import tn.anasazx.tunirate.product.dto.ProductResponse;
import tn.anasazx.tunirate.product.entity.Product;
import tn.anasazx.tunirate.product.entity.ProductImage;

import java.util.List;
import java.util.Optional;

@Component
public class ProductMapper {
    public static ProductResponse mapProductToResponse(Product product) {

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
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                categoryName,
                subcategoryName,
                product.getCompany().getId(),
                product.getCompany().getName(),
                product.getCompany().getVerified(),
                product.getCompany().getLogoUrl(),
                imageUrl.orElse(null),
                product.getCreatedAt()
        );

    }

    public static ProductDetailsResponse mapProductToDetailsResponse(Product product, double avgRating, long reviewsCount) {

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

        return new ProductDetailsResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                categoryName,
                subcategoryName,
                product.getCompany().getId(),
                product.getCompany().getName(),
                product.getCompany().getVerified(),
                product.getCompany().getLogoUrl(),
                avgRating,
                reviewsCount,
                images
        );
    }

    public static Product toProduct(ProductRequest request, Company company, Subcategory subcategory) {
        Product product = new Product();
        updateProduct(product, request, company, subcategory);
        return product;
    }

    public static void updateProduct(Product product, ProductRequest request, Company company, Subcategory subcategory) {
        product.setName(request.name());
        product.setDescription(request.description());
        product.setCompany(company);
        product.setSubcategory(subcategory);
    }

}
