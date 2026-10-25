package tn.anasazx.tunirate.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.enums.CompanyRole;
import tn.anasazx.tunirate.membership.repository.CompanyMemberRepository;

/**
 * Company portal access is based on company membership, not on the global role.
 * Used in @PreAuthorize, e.g. @PreAuthorize("@companyAccess.isMember()").
 */
@Component("companyAccess")
@RequiredArgsConstructor
public class CompanyAccess {

    private final CompanyMemberRepository companyMemberRepository;

    // Current user belongs to a company (HEAD or WORKER)
    public boolean isMember() {
        Long currentUserId = SecurityUtils.getCurrentUserIdOrNull();
        return currentUserId != null && companyMemberRepository.findFirstByUserId(currentUserId).isPresent();
    }

    // Current user is the HEAD of their company
    public boolean isHead() {
        Long currentUserId = SecurityUtils.getCurrentUserIdOrNull();
        return currentUserId != null && companyMemberRepository.findFirstByUserId(currentUserId)
                .map(member -> member.getCompanyRole() == CompanyRole.HEAD)
                .orElse(false);
    }

}
