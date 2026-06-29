package tn.anasazx.tunirate.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.anasazx.tunirate.company.entity.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

	Optional<Company> findByNameIgnoreCase(String name);

	@Query("""
	SELECT DISTINCT c FROM Company c
	LEFT JOIN Product p ON p.company = c
	WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :q, '%'))
	   OR LOWER(p.name) LIKE LOWER(CONCAT('%', :q, '%'))
	""")
	List<Company> search(@Param("q") String q);


	Long countByVerified(Boolean verified);

}

