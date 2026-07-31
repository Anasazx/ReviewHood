package tn.anasazx.tunirate.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.anasazx.tunirate.enums.ProductStatus;
import tn.anasazx.tunirate.product.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
    SELECT p FROM Product p
    WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :q, '%'))
    OR LOWER(p.description) LIKE LOWER(CONCAT('%', :q, '%'))
    """)
    List<Product> search(@Param("q") String q);

    Page<Product> findAllByStatus(ProductStatus status, Pageable pageable);

    Page<Product> findAllByStatusAndSubcategoryId(ProductStatus status, Long subcategoryId, Pageable pageable);

    Page<Product> findAllByStatusAndSubcategoryCategoryId(ProductStatus status, Long categoryId, Pageable pageable);

    List<Product> findByCompanyId(Long companyId);

    Page<Product> findByCompanyIdAndStatus(Long companyId, ProductStatus status, Pageable pageable);

    Optional<Product> findByStatusAndId(ProductStatus status, Long id);

    Optional<Product> findByStatusNotAndId(ProductStatus status, Long id);

    List<Product> findAllByStatusNot(ProductStatus status);

    Long countByCompanyId(Long companyId);

    Long countByStatus(ProductStatus status);


    // --- review stats maintenance ---

    @Modifying
    @Query(value = """
        UPDATE products p
        SET reviews_avg = COALESCE((SELECT AVG(r.rating) FROM reviews r WHERE r.product_id = :productId), 0),
            review_count = (SELECT COUNT(*) FROM reviews r WHERE r.product_id = :productId)
        WHERE p.id = :productId
        """, nativeQuery = true)
    void recalculateReviewStats(@Param("productId") Long productId);

    @Modifying
    @Query(value = """
        UPDATE products p
        SET reviews_avg = COALESCE((SELECT AVG(r.rating) FROM reviews r WHERE r.product_id = p.id), 0),
            review_count = (SELECT COUNT(*) FROM reviews r WHERE r.product_id = p.id)
        WHERE p.reviews_avg != COALESCE((SELECT AVG(r.rating) FROM reviews r WHERE r.product_id = p.id), 0)
           OR p.review_count != (SELECT COUNT(*) FROM reviews r WHERE r.product_id = p.id)
        """, nativeQuery = true)
    int reconcileReviewStats();

    @Modifying
    @Query("UPDATE Product p SET p.reviewCount = p.reviewCount + 1 WHERE p.id = :productId")
    void incrementReviewCount(@Param("productId") Long productId);

    @Modifying
    @Query("UPDATE Product p SET p.reviewCount = p.reviewCount - 1 WHERE p.id = :productId AND p.reviewCount > 0")
    void decrementReviewCount(@Param("productId") Long productId);
}