package tn.anasazx.tunirate.invitation.dto;

import lombok.Builder;
import lombok.Data;
import tn.anasazx.tunirate.enums.InvitationStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class CompanyInvitationResponse {
    private Long id;
    private Long companyId;
    private String companyName;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long invitedById;
    private String invitedByName;
    private InvitationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean expired;
}