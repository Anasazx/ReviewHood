package tn.anasazx.tunirate.invitation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.anasazx.tunirate.invitation.dto.CompanyInvitationRequest;
import tn.anasazx.tunirate.invitation.dto.CompanyInvitationResponse;
import tn.anasazx.tunirate.invitation.service.CompanyInvitationService;
import tn.anasazx.tunirate.security.SecurityUtils;

import java.util.List;

@RestController
@RequestMapping("/invitations")
@RequiredArgsConstructor
public class CompanyInvitationController {

    private final CompanyInvitationService service;

    // CREATE invitation (company sends invite)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> sendInvitation(@RequestBody CompanyInvitationRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        service.sendInvitation(request, currentUserId);
        return ResponseEntity.ok().build();
    }

    // GET invitations for company (HEAD only handled in service)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/company")
    public ResponseEntity<List<CompanyInvitationResponse>> getCompanyInvitations() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(service.getCompanyInvitations(currentUserId));
    }

    // GET invitations for current user
    @GetMapping("/me")
    public ResponseEntity<List<CompanyInvitationResponse>> getMyInvitations() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(service.getUserInvitations(currentUserId));
    }

    // ACCEPT invitation
    @PostMapping("/{invitationId}/accept")
    public ResponseEntity<CompanyInvitationResponse> acceptInvitation(@PathVariable Long invitationId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(service.acceptInvitation(invitationId, currentUserId));
    }

    // REJECT invitation
    @PostMapping("/{invitationId}/reject")
    public ResponseEntity<CompanyInvitationResponse> rejectInvitation(@PathVariable Long invitationId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(service.rejectInvitation(invitationId, currentUserId));
    }

    // CANCEL invitation (company head only)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{invitationId}/cancel")
    public ResponseEntity<CompanyInvitationResponse> cancelInvitation(@PathVariable Long invitationId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(service.cancelInvitation(invitationId, currentUserId));
    }
}