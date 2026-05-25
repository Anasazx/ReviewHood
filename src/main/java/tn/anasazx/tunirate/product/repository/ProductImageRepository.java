package tn.anasazx.tunirate.product.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.anasazx.tunirate.product.entity.ProductImage;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductId(Long productId);
    long countByProductId(Long productId);
    Optional<ProductImage> findByProductIdAndIsMainTrue(Long productId);

    boolean existsByProductId(Long productId);

    @Modifying
    @Query("UPDATE ProductImage p SET p.isMain = false WHERE p.product.id = :productId")
    void clearMainImages(@Param("productId") Long productId);

}