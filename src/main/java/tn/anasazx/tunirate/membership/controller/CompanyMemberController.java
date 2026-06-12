package tn.anasazx.tunirate.membership.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.anasazx.tunirate.membership.dto.CompanyMemberRequest;
import tn.anasazx.tunirate.membership.dto.CompanyMemberResponse;
import tn.anasazx.tunirate.membership.dto.UpdateMemberRoleRequest;
import tn.anasazx.tunirate.membership.service.CompanyMemberService;

import java.util.List;

@RestController
@RequestMapping("/membership")
@RequiredArgsConstructor
public class CompanyMemberController {

    private final CompanyMemberService service;

    @PostMapping("/assign")
    public ResponseEntity<CompanyMemberResponse> assignMemberToCompany(@RequestBody CompanyMemberRequest request) {
        CompanyMemberResponse response = service.assignUserToCompany(request.userId(), request.companyId(), request.role());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeMemberFromCompany(@RequestParam Long userId, @RequestParam Long companyId) {
        service.removeUserFromCompany(userId, companyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<CompanyMemberResponse>> getMembersByCompanyId(@PathVariable Long companyId) {
        return ResponseEntity.ok(
                service.getMembersByCompany(companyId)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CompanyMemberResponse> getCompaniesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(
                service.getCompanyByUserId(userId)
        );
    }

    @PatchMapping("/role")
    public ResponseEntity<CompanyMemberResponse> updateRole(@RequestBody UpdateMemberRoleRequest request) {
        return ResponseEntity.ok(
                service.updateRole(request.userId(), request.companyId(), request.role())
        );
    }

}