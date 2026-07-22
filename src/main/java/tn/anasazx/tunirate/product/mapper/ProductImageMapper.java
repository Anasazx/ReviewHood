package tn.anasazx.tunirate.product.mapper;

import tn.anasazx.tunirate.product.dto.ProductImageResponse;
import tn.anasazx.tunirate.product.entity.ProductImage;

import java.util.List;

public class ProductImageMapper {

    private static final String UPLOAD_URL = "/uploads/";

    public static ProductImageResponse toResponse(ProductImage image) {
        System.out.println("response exemple: " + UPLOAD_URL + image.getUrl());
        return new ProductImageResponse(
                image.getId(),
                UPLOAD_URL + image.getUrl(),
                image.isMain()
        );
    }

    public static List<ProductImageResponse> toResponseList(List<ProductImage> images) {
        return images.stream()
                .map(ProductImageMapper::toResponse)
                .toList();
    }
}