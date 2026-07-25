package tn.anasazx.tunirate.emailVerification.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.anasazx.tunirate.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String codeHash;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private int attempts = 0;

    // Number of emails sent
    @Column(nullable = false)
    private int resendCount = 0;

    // Last time we sent a code
    @Column(nullable = false)
    private LocalDateTime lastSentAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime resendResetAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastSentAt = createdAt;
        expiresAt = createdAt.plusMinutes(10);
        resendResetAt = createdAt.plusDays(1);
    }

}
