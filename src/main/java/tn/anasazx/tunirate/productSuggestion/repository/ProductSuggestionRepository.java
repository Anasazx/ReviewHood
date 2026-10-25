package tn.anasazx.tunirate.productSuggestion.repository;

import tn.anasazx.tunirate.enums.SuggestionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.anasazx.tunirate.productSuggestion.entity.ProductSuggestion;

import java.util.List;

@Repository
public interface ProductSuggestionRepository extends JpaRepository<ProductSuggestion, Long> {
    List<ProductSuggestion> findAllByUserId(Long userId);

    List<ProductSuggestion> findAllByOrderByCreatedAtDesc();

    List<ProductSuggestion> findAllByStatusOrderByCreatedAtDesc(SuggestionStatus status);

    long countByStatus(SuggestionStatus status);
}
