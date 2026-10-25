package tn.anasazx.tunirate.company.repository;

import org.springframework.data.jpa.domain.Specification;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.enums.CompanyStatus;

public class CompanySpecifications {

    public static Specification<Company> hasNameLike(String q) {
        return (root, query, cb) ->
                (q == null || q.isBlank()) ? null
                        : cb.like(cb.lower(root.get("name")), "%" + q.trim().toLowerCase() + "%");
    }

    public static Specification<Company> hasStatus(CompanyStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

}
