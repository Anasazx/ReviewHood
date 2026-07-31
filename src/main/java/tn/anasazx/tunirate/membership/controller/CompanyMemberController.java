package tn.anasazx.tunirate.membership.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/assign")
    public ResponseEntity<CompanyMemberResponse> assignMemberToCompany(@RequestBody CompanyMemberRequest request) {
        CompanyMemberResponse response = service.assignUserToCompany(request.userId(), request.companyId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeMemberFromCompany(@RequestParam Long userId, @RequestParam Long companyId) {
        service.removeUserFromCompany(userId, companyId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeMemberFromMyCompany(@RequestParam Long userId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        service.removeUserFromMyCompany(userId, currentUserId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<CompanyMemberResponse>> getMembersByCompanyId(@PathVariable Long companyId) {
        return ResponseEntity.ok(
                service.getMembersByCompanyId(companyId)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CompanyMemberResponse> getCompaniesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(
                service.getCompanyByUserId(userId)
        );
    }

    @GetMapping
    public ResponseEntity<List<CompanyMemberResponse>> getMyCompanyMembers() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(service.getMyCompanyMembers(currentUserId));
    }

    @PatchMapping("/role")
    public ResponseEntity<CompanyMemberResponse> updateRole(@RequestBody UpdateMemberRoleRequest request) {
        return ResponseEntity.ok(
                service.updateRole(request.userId(), request.companyId(), request.companyRole())
        );
    }

}