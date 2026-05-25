package tn.anasazx.tunirate.product.service;


import org.springframework.web.multipart.MultipartFile;
import tn.anasazx.tunirate.product.dto.ProductImageResponse;

import java.util.List;

public interface ProductImageService {
    ProductImageResponse addImage(Long productId, MultipartFile file);
    void deleteImage(Long imageId);
    void setMainImage(Long productId, Long imageId);
    List<ProductImageResponse> getImagesByProductId(Long productId);
}