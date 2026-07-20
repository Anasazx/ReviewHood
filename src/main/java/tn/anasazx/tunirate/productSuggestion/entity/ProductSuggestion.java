package tn.anasazx.tunirate.productSuggestion.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.anasazx.tunirate.enums.SuggestionStatus;
import tn.anasazx.tunirate.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_suggestions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String companyName;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SuggestionStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = SuggestionStatus.PENDING;
        }
    }

}