package tn.anasazx.tunirate.invitation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.anasazx.tunirate.invitation.dto.CompanyInvitationRequest;
import tn.anasazx.tunirate.invitation.dto.CompanyInvitationResponse;
import tn.anasazx.tunirate.invitation.service.CompanyInvitationService;

import java.util.List;

@RestController
@RequestMapping("/invitations")
@RequiredArgsConstructor
public class CompanyInvitationController {

    private final CompanyInvitationService service;

    // CREATE invitation (company sends invite)
    @PostMapping
    public ResponseEntity<Void> sendInvitation(@RequestBody CompanyInvitationRequest request) {
        service.sendInvitation(request);
        return ResponseEntity.ok().build();
    }

    // GET invitations for company (HEAD only handled in service)
    @GetMapping("/company")
    public ResponseEntity<List<CompanyInvitationResponse>> getCompanyInvitations() {
        return ResponseEntity.ok(service.getCompanyInvitations());
    }

    // GET invitations for current user
    @GetMapping("/me")
    public ResponseEntity<List<CompanyInvitationResponse>> getMyInvitations() {
        return ResponseEntity.ok(service.getUserInvitations());
    }

    // ACCEPT invitation
    @PostMapping("/{invitationId}/accept")
    public ResponseEntity<CompanyInvitationResponse> acceptInvitation(@PathVariable Long invitationId) {
        return ResponseEntity.ok(service.acceptInvitation(invitationId));
    }

    // REJECT invitation
    @PostMapping("/{invitationId}/reject")
    public ResponseEntity<CompanyInvitationResponse> rejectInvitation(@PathVariable Long invitationId) {
        return ResponseEntity.ok(service.rejectInvitation(invitationId));
    }

    // CANCEL invitation (company head only)
    @PostMapping("/{invitationId}/cancel")
    public ResponseEntity<CompanyInvitationResponse> cancelInvitation(@PathVariable Long invitationId) {
        return ResponseEntity.ok(service.cancelInvitation(invitationId));
    }
}