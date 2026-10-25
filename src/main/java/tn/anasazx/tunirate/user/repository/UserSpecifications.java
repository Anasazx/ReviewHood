package tn.anasazx.tunirate.user.repository;

import org.springframework.data.jpa.domain.Specification;
import tn.anasazx.tunirate.enums.GlobalRole;
import tn.anasazx.tunirate.enums.UserStatus;
import tn.anasazx.tunirate.user.entity.User;

public class UserSpecifications {

    // Matches the name or the email
    public static Specification<User> matchesQuery(String q) {
        return (root, query, cb) -> {
            if (q == null || q.isBlank()) return null;
            String pattern = "%" + q.trim().toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("email")), pattern)
            );
        };
    }

    public static Specification<User> hasRole(GlobalRole role) {
        return (root, query, cb) ->
                role == null ? null : cb.equal(root.get("globalRole"), role);
    }

    public static Specification<User> hasStatus(UserStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

}
