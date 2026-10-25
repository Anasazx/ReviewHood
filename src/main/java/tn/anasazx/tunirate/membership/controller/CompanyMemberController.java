package tn.anasazx.tunirate.membership.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.anasazx.tunirate.membership.dto.CompanyMemberRequest;
import tn.anasazx.tunirate.membership.dto.CompanyMemberResponse;
import tn.anasazx.tunirate.membership.dto.UpdateMemberRoleRequest;
import tn.anasazx.tunirate.membership.service.CompanyMemberService;
import tn.anasazx.tunirate.security.SecurityUtils;

import java.util.List;

@RestController
@RequestMapping("/membership")
@RequiredArgsConstructor
public class CompanyMemberController {

    private final CompanyMemberService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assign")
    public ResponseEntity<CompanyMemberResponse> assignMemberToCompany(@RequestBody CompanyMemberRequest request) {
        CompanyMemberResponse response = service.assignUserToCompany(request.userId(), request.companyId(), request.companyRole());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeMemberFromCompany(@RequestParam Long userId, @RequestParam Long companyId) {
        service.removeUserFromCompany(userId, companyId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("@companyAccess.isHead()")
    @DeleteMapping
    public ResponseEntity<Void> removeMemberFromMyCompany(@RequestParam Long userId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        service.removeUserFromMyCompany(userId, currentUserId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<CompanyMemberResponse>> getMembersByCompanyId(@PathVariable Long companyId) {
        return ResponseEntity.ok(
                service.getMembersByCompanyId(companyId)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<CompanyMemberResponse> getCompaniesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(
                service.getCompanyByUserId(userId)
        );
    }

    @PreAuthorize("@companyAccess.isHead()")
    @GetMapping
    public ResponseEntity<List<CompanyMemberResponse>> getMyCompanyMembers() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(service.getMyCompanyMembers(currentUserId));
    }

    // The current user's own membership (company and role), for the company portal
    @PreAuthorize("@companyAccess.isMember()")
    @GetMapping("/me")
    public ResponseEntity<CompanyMemberResponse> getMyMembership() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(service.getCompanyByUserId(currentUserId));
    }

    // The company head changes the role of someone in their own company
    @PreAuthorize("@companyAccess.isHead()")
    @PatchMapping("/my/role")
    public ResponseEntity<CompanyMemberResponse> updateRoleInMyCompany(@RequestBody UpdateMemberRoleRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(service.updateRoleInMyCompany(request.userId(), request.companyRole(), currentUserId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/role")
    public ResponseEntity<CompanyMemberResponse> updateRole(@RequestBody UpdateMemberRoleRequest request) {
        return ResponseEntity.ok(
                service.updateRole(request.userId(), request.companyId(), request.companyRole())
        );
    }

}