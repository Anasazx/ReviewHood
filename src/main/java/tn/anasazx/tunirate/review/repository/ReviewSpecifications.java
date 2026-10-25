package tn.anasazx.tunirate.review.repository;

import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import tn.anasazx.tunirate.comment.entity.Comment;
import tn.anasazx.tunirate.review.entity.Review;

public class ReviewSpecifications {

    public static Specification<Review> forCompany(Long companyId) {
        return (root, query, cb) -> cb.equal(root.get("product").get("company").get("id"), companyId);
    }

    public static Specification<Review> hasRating(Integer rating) {
        return (root, query, cb) -> rating == null ? null : cb.equal(root.get("rating"), rating);
    }

    public static Specification<Review> hasProductId(Long productId) {
        return (root, query, cb) -> productId == null ? null : cb.equal(root.get("product").get("id"), productId);
    }

    // Reviews the company has not commented on yet
    public static Specification<Review> notAnsweredBy(Long companyId, Boolean unanswered) {
        return (root, query, cb) -> {
            if (!Boolean.TRUE.equals(unanswered)) return null;
            Subquery<Long> sub = query.subquery(Long.class);
            Root<Comment> comment = sub.from(Comment.class);
            sub.select(comment.get("id")).where(
                    cb.equal(comment.get("review"), root),
                    cb.equal(comment.get("actor").get("id"), companyId),
                    cb.isFalse(comment.get("deleted"))
            );
            return cb.not(cb.exists(sub));
        };
    }

}
