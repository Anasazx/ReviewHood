package tn.anasazx.tunirate.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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

    List<Product> findByCompanyId(Long companyId);

    Page<Product> findByCompanyIdAndStatus(Long companyId, ProductStatus status, Pageable pageable);

    Optional<Product> findByStatusAndId(ProductStatus status, Long id);

    Optional<Product> findByStatusNotAndId(ProductStatus status, Long id);

    List<Product> findAllByStatusNot(ProductStatus status);

    Long countByCompanyId(Long companyId);

    Long countByStatus(ProductStatus status);

}

