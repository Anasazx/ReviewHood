package tn.anasazx.tunirate.membership.service;

import tn.anasazx.tunirate.enums.CompanyRole;
import tn.anasazx.tunirate.membership.dto.CompanyMemberResponse;

import java.util.List;

public interface CompanyMemberService {
    CompanyMemberResponse assignUserToCompany(Long userId, Long companyId, CompanyRole role);
    void removeUserFromCompany(Long userId, Long companyId);
    List<CompanyMemberResponse> getMembersByCompany(Long companyId);
    List<CompanyMemberResponse> getCompaniesByUser(Long userId);
    boolean isUserInCompany(Long userId, Long companyId);

}