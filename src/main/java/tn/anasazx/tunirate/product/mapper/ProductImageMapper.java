package tn.anasazx.tunirate.product.mapper;

import tn.anasazx.tunirate.product.dto.ProductImageResponse;
import tn.anasazx.tunirate.product.entity.ProductImage;

import java.util.List;

public class ProductImageMapper {

    // single entity → DTO
    public static ProductImageResponse toDto(ProductImage image) {
        return new ProductImageResponse(
                image.getId(),
                image.getUrl(),
                image.isMain()
        );
    }

    // list of entities → list of DTOs
    public static List<ProductImageResponse> toDtoList(List<ProductImage> images) {
        return images.stream()
                .map(ProductImageMapper::toDto)
                .toList();
    }
}