package tn.anasazx.tunirate.membership.service;

import tn.anasazx.tunirate.enums.CompanyRole;
import tn.anasazx.tunirate.membership.dto.CompanyMemberResponse;

import java.util.List;

public interface CompanyMemberService {
    CompanyMemberResponse assignUserToCompany(Long userId, Long companyId);
    void removeUserFromCompany(Long userId, Long companyId);
    List<CompanyMemberResponse> getMembersByCompanyId(Long companyId);
    CompanyMemberResponse getCompanyByUserId(Long userId);
    boolean isUserInCompany(Long userId, Long companyId);

    boolean isUserHeadInCompany(Long userId, Long companyId);

    List<CompanyMemberResponse> getMyCompanyMembers(Long currentUserId);

    CompanyMemberResponse updateRole(Long  userId, Long companyId, CompanyRole role);

    void removeUserFromMyCompany(Long removedUserId, Long currentUserId);

}