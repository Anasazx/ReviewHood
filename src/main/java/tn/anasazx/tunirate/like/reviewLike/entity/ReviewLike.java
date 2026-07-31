package tn.anasazx.tunirate.like.reviewLike.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.anasazx.tunirate.review.entity.Review;
import tn.anasazx.tunirate.user.entity.User;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "review_likes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "review_id"})
        },
        indexes = {
                @Index(name = "idx_review_likes_review_id", columnList = "review_id"),
                @Index(name = "idx_review_likes_user_id", columnList = "user_id")
        }
)
public class ReviewLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}