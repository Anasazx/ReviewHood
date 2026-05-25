package tn.anasazx.tunirate.membership.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.anasazx.tunirate.enums.CompanyRole;
import tn.anasazx.tunirate.membership.entity.CompanyMember;
import tn.anasazx.tunirate.membership.service.CompanyMemberService;

import java.util.List;

@RestController
@RequestMapping("/company-members")
@RequiredArgsConstructor
public class CompanyMemberController {

    private final CompanyMemberService service;

    @PostMapping("/assign")
    public CompanyMember assign(@RequestParam Long userId,
                                @RequestParam Long companyId,
                                @RequestParam CompanyRole role) {
        return service.assignUserToCompany(userId, companyId, role);
    }

    @DeleteMapping("/remove")
    public void remove(@RequestParam Long userId,
                       @RequestParam Long companyId) {
        service.removeUserFromCompany(userId, companyId);
    }

    @GetMapping("/company/{companyId}")
    public List<CompanyMember> getByCompany(@PathVariable Long companyId) {
        return service.getMembersByCompany(companyId);
    }

    @GetMapping("/user/{userId}")
    public List<CompanyMember> getByUser(@PathVariable Long userId) {
        return service.getCompaniesByUser(userId);
    }

}