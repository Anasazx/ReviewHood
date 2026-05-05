package tn.anasazx.tunirate.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.anasazx.tunirate.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

