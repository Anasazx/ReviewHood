package tn.anasazx.tunirate.comment.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.anasazx.tunirate.actor.entity.Actor;
import tn.anasazx.tunirate.user.entity.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(
        name = "comments",
        indexes = {
                @Index(name = "idx_comments_review_id", columnList = "review_id"),
                @Index(name = "idx_comments_actor_id", columnList = "actor_id"),
                @Index(name = "idx_comments_parent_comment_id", columnList = "parent_comment_id"),
                @Index(name = "idx_comments_posted_by_id", columnList = "posted_by_id")

        }
)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;


    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;


    @ManyToOne
    @JoinColumn(name = "actor_id", nullable = false)
    private Actor actor;

    @ManyToOne
    @JoinColumn(name = "posted_by_id")
    private User postedBy; // INTERNAL — which employee posted it, null if actor is a regular user


    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private tn.anasazx.tunirate.review.entity.Review review;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment repliedTo;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}