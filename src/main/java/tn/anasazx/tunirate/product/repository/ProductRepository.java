package tn.anasazx.tunirate.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.anasazx.tunirate.enums.ProductStatus;
import tn.anasazx.tunirate.product.entity.Product;
import tn.anasazx.tunirate.subcategory.entity.Subcategory;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("""
    SELECT p
    FROM Product p
    WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :q, '%'))
       OR LOWER(p.description) LIKE LOWER(CONCAT('%', :q, '%'))
    """)
    Page<Product> search(@Param("q") String q, Pageable pageable);

    Page<Product> findAllByStatus(ProductStatus status, Pageable pageable);

    Page<Product> findAllByStatusAndSubcategoryId(ProductStatus status, Long subcategoryId, Pageable pageable);

    Page<Product> findAllByStatusAndSubcategoryCategoryId(ProductStatus status, Long categoryId, Pageable pageable);

    List<Product> findByCompanyId(Long companyId);

    Optional<Product> findByStatusAndId(ProductStatus status, Long id);

    Optional<Product> findByStatusNotAndId(ProductStatus status, Long id);

    List<Product> findAllByStatusNot(ProductStatus status);

    Long countByCompanyId(Long companyId);

    Long countByStatus(ProductStatus status);

    Page<Product> findAllBySubcategoryIsNull(Pageable pageable);


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

    List<Product> findAllByOrderByCreatedAtDesc(PageRequest of);


    @Query("""
    SELECT p
    FROM Product p
    WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))
    ORDER BY
        CASE
            WHEN LOWER(p.name) = LOWER(:query) THEN 0
            WHEN LOWER(p.name) LIKE LOWER(CONCAT(:query, '%')) THEN 1
            ELSE 2
        END,
        p.name ASC
    """)
    List<Product> findSuggestions(
            @Param("query") String query,
            Pageable pageable
    );


    @Query("""
        SELECT DISTINCT p.subcategory
        FROM Product p
        WHERE p.company.id = :companyId
    """)
    List<Subcategory> findDistinctSubcategoriesByCompanyId(@Param("companyId") Long companyId);


}