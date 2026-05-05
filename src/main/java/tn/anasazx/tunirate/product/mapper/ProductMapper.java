package tn.anasazx.tunirate.product.mapper;

import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.product.dto.ProductDetailsResponse;
import tn.anasazx.tunirate.product.dto.ProductImageResponse;
import tn.anasazx.tunirate.product.dto.ProductResponse;
import tn.anasazx.tunirate.product.entity.Product;
import tn.anasazx.tunirate.review.dto.ReviewResponse;

import java.util.List;

@Component
public class ProductMapper {
    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getCompany().getId(),
                product.getCompany().getName(),
                product.getCreatedAt()
        );
    }

    public ProductDetailsResponse toDetailsResponse(
            Product product,
            List<ReviewResponse> reviews,
            double avgRating,
            long reviewsCount
    ) {

        List<ProductImageResponse> images = product.getImages()
                .stream()
                .map(img -> new ProductImageResponse(
                        img.getId(),
                        img.getUrl(),
                        img.isMain()
                ))
                .toList();

        return new ProductDetailsResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getCompany().getName(),
                avgRating,
                reviewsCount,
                reviews,
                images
                );
    }
}
