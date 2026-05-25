package tn.anasazx.tunirate.categories.subcategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.anasazx.tunirate.categories.subcategory.entity.Subcategory;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
}