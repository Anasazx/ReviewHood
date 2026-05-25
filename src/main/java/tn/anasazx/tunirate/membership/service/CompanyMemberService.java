package tn.anasazx.tunirate.membership.service;

import tn.anasazx.tunirate.enums.CompanyRole;
import tn.anasazx.tunirate.membership.entity.CompanyMember;

import java.util.List;

public interface CompanyMemberService {
    CompanyMember assignUserToCompany(Long userId, Long companyId, CompanyRole role);
    void removeUserFromCompany(Long userId, Long companyId);
    List<CompanyMember> getMembersByCompany(Long companyId);
    List<CompanyMember> getCompaniesByUser(Long userId);
    boolean isUserInCompany(Long userId, Long companyId);

}