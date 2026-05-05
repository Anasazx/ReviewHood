package tn.anasazx.tunirate.product.service;


import tn.anasazx.tunirate.product.entity.ProductImage;

import java.util.List;

public interface ProductImageService {
    ProductImage addImage(Long productId, String url);
    void deleteImage(Long imageId);
    void setMainImage(Long productId, Long imageId);
    List<ProductImage> getImagesByProductId(Long productId);
}