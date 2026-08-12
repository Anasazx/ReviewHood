package tn.anasazx.tunirate.company.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.enums.CompanyStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

	Optional<Company> findByNameIgnoreCase(String name);

	@Query("""
    SELECT DISTINCT c
    FROM Company c
    LEFT JOIN Product p ON p.company = c
    WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :q, '%'))
       OR LOWER(p.name) LIKE LOWER(CONCAT('%', :q, '%'))
    """)
	Page<Company> search(@Param("q") String q, Pageable pageable);

	Long countByStatus(CompanyStatus status);


	// CompanyRepository.java
	@Query(value = """
    SELECT CAST(created_at AS date) AS day, COUNT(*) AS count
    FROM companies
    WHERE created_at >= :startDate
    GROUP BY CAST(created_at AS date)
    ORDER BY day
    """, nativeQuery = true)
	List<Object[]> countCompaniesByDayRaw(@Param("startDate") LocalDateTime startDate);

	@Query("""
    SELECT c
    FROM Company c
    WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))
    ORDER BY
        CASE
            WHEN LOWER(c.name) = LOWER(:query) THEN 0
            WHEN LOWER(c.name) LIKE LOWER(CONCAT(:query, '%')) THEN 1
            ELSE 2
        END,
        c.name ASC
    """)
	List<Company> findSuggestions(
			@Param("query") String query,
			Pageable pageable
	);
}

