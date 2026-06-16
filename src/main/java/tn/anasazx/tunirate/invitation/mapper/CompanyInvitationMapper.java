package tn.anasazx.tunirate.invitation.mapper;

import tn.anasazx.tunirate.invitation.dto.CompanyInvitationResponse;
import tn.anasazx.tunirate.invitation.entity.CompanyInvitation;

public class CompanyInvitationMapper {

    public static CompanyInvitationResponse toResponse(CompanyInvitation inv) {

        if (inv == null) return null;

        return CompanyInvitationResponse.builder()
                .id(inv.getId())
                .companyId(inv.getCompany().getId())
                .companyName(inv.getCompany().getName())
                .userId(inv.getUser().getId())
                .userName(inv.getUser().getName())
                .userEmail(inv.getUser().getEmail())
                .invitedById(inv.getInvitedBy().getId())
                .invitedByName(inv.getInvitedBy().getName())
                .status(inv.getStatus())
                .createdAt(inv.getCreatedAt())
                .expiresAt(inv.getExpiresAt())
                .expired(inv.isExpired())
                .build();
    }

}