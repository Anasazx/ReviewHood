package tn.anasazx.tunirate.product.repository;

import org.springframework.data.jpa.domain.Specification;
import tn.anasazx.tunirate.enums.ProductStatus;
import tn.anasazx.tunirate.product.entity.Product;

public class ProductSpecifications {

    public static Specification<Product> hasCompanyId(Long companyId) {
        return (root, query, cb) ->
                companyId == null ? null : cb.equal(root.get("company").get("id"), companyId);
    }

    public static Specification<Product> hasStatus(ProductStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Product> hasNameLike(String name) {
        return (root, query, cb) ->
                (name == null || name.isBlank()) ? null
                        : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    // Matches the product name or its company name
    public static Specification<Product> matchesQuery(String q) {
        return (root, query, cb) -> {
            if (q == null || q.isBlank()) return null;
            String pattern = "%" + q.trim().toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("company").get("name")), pattern)
            );
        };
    }

    public static Specification<Product> hasStatusNot(ProductStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.notEqual(root.get("status"), status);
    }

    public static Specification<Product> isUncategorized(Boolean uncategorized) {
        return (root, query, cb) ->
                !Boolean.TRUE.equals(uncategorized) ? null : cb.isNull(root.get("subcategory"));
    }

    public static Specification<Product> hasSubcategoryId(Long subcategoryId) {
        return (root, query, cb) ->
                subcategoryId == null ? null : cb.equal(root.get("subcategory").get("id"), subcategoryId);
    }
}