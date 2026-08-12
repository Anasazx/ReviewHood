package tn.anasazx.tunirate.subcategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.anasazx.tunirate.subcategory.entity.Subcategory;

import java.util.List;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
    List<Subcategory> findByCategoryName(String categoryName);
}