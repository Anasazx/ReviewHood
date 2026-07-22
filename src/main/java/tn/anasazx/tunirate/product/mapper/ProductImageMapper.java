package tn.anasazx.tunirate.product.mapper;

import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.product.dto.ProductImageResponse;
import tn.anasazx.tunirate.product.entity.ProductImage;

import java.util.List;

@Component
public class ProductImageMapper {

    public static ProductImageResponse toResponse(ProductImage image) {
        return new ProductImageResponse(
                image.getId(),
                image.getUrl(),
                image.isMain()
        );
    }

    public static List<ProductImageResponse> toResponseList(List<ProductImage> images) {
        return images.stream()
                .map(ProductImageMapper::toResponse)
                .toList();
    }
}