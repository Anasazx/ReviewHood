package tn.anasazx.tunirate.product.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tn.anasazx.tunirate.product.entity.ProductImage;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductId(Long productId);
    long countByProductId(Long productId);
}