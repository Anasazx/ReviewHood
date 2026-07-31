package tn.anasazx.tunirate.review.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.anasazx.tunirate.comment.entity.Comment;
import tn.anasazx.tunirate.like.reviewLike.entity.ReviewLike;
import tn.anasazx.tunirate.product.entity.Product;
import tn.anasazx.tunirate.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "reviews",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "product_id"})
        },
        indexes = {
                @Index(name = "idx_reviews_product_id", columnList = "product_id"),
                @Index(name = "idx_reviews_user_id", columnList = "user_id")
        }
)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Rating is the core value of your system
    @Column(nullable = false)
    private Integer rating; // 1 to 5

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Long likeCount = 0L;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(
            mappedBy = "review",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewLike> likes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.likeCount == null) {
            this.likeCount = 0L;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}