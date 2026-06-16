package tn.anasazx.tunirate.invitation.service;

import tn.anasazx.tunirate.invitation.dto.CompanyInvitationRequest;
import tn.anasazx.tunirate.invitation.dto.CompanyInvitationResponse;

import java.util.List;

public interface CompanyInvitationService {
    void sendInvitation(CompanyInvitationRequest request);
    CompanyInvitationResponse acceptInvitation(Long invitationId);
    CompanyInvitationResponse rejectInvitation(Long invitationId);
    CompanyInvitationResponse cancelInvitation(Long invitationId);
    List<CompanyInvitationResponse> getCompanyInvitations();
    List<CompanyInvitationResponse> getUserInvitations();
}
