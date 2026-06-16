package tn.anasazx.tunirate.invitation.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.enums.InvitationStatus;
import tn.anasazx.tunirate.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(
        indexes = {
                @Index(name = "idx_inv_user", columnList = "user_id"),
                @Index(name = "idx_inv_company", columnList = "company_id"),
                @Index(name = "idx_inv_status", columnList = "status")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Company company;

    @ManyToOne
    private User user;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private InvitationStatus status = InvitationStatus.PENDING;

    @ManyToOne
    private User invitedBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiresAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.expiresAt = this.createdAt.plusDays(7);
    }



    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }

}