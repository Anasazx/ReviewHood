package tn.anasazx.tunirate.invitation.dto;


import lombok.Data;

@Data
public class CompanyInvitationRequest {
    private String invitedUserEmail; // the person being invited
}