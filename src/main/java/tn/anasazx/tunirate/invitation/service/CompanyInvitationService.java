package tn.anasazx.tunirate.invitation.service;

import org.springframework.transaction.annotation.Transactional;
import tn.anasazx.tunirate.invitation.dto.CompanyInvitationRequest;
import tn.anasazx.tunirate.invitation.dto.CompanyInvitationResponse;

import java.util.List;

public interface CompanyInvitationService {

    void sendInvitation(CompanyInvitationRequest request, Long currentUserId);

    @Transactional
    CompanyInvitationResponse acceptInvitation(Long invitationId, Long currentUserId);

    @Transactional
    CompanyInvitationResponse rejectInvitation(Long invitationId, Long currentUserId);

    @Transactional
    CompanyInvitationResponse cancelInvitation(Long invitationId, Long currentUserId);

    List<CompanyInvitationResponse> getCompanyInvitations(Long currentUserId);

    List<CompanyInvitationResponse> getUserInvitations(Long currentUserId);

}
