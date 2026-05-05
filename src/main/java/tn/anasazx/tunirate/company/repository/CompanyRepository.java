package tn.anasazx.tunirate.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.anasazx.tunirate.company.entity.Company;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

	Optional<Company> findByNameIgnoreCase(String name);
}

