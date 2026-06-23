package tn.anasazx.tunirate.category.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tn.anasazx.tunirate.category.entity.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByOrderByNameAsc();
}