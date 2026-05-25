package tn.anasazx.tunirate.categories.category.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tn.anasazx.tunirate.categories.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}