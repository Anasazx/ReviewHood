package tn.anasazx.tunirate.membership.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.company.service.CompanyService;
import tn.anasazx.tunirate.enums.CompanyRole;
import tn.anasazx.tunirate.membership.dto.CompanyMemberResponse;
import tn.anasazx.tunirate.membership.entity.CompanyMember;
import tn.anasazx.tunirate.membership.mapper.CompanyMemberMapper;
import tn.anasazx.tunirate.membership.repository.CompanyMemberRepository;
import tn.anasazx.tunirate.membership.service.CompanyMemberService;
import tn.anasazx.tunirate.security.SecurityUtils;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyMemberServiceImpl implements CompanyMemberService {

    private final CompanyMemberRepository companyMemberRepository;
    private final UserService userService;
    private final CompanyService companyService;

    //TODO: i need to add a methode "getMyCompanyMembers()" that takes no params and return the members of a users after searching the company id from his id



    @Override
    public CompanyMemberResponse assignUserToCompany(Long userId, Long companyId) {

        if (companyMemberRepository.existsByUserIdAndCompanyId(userId, companyId)) {
            throw new RuntimeException("User already in this company");
        }

        User user = userService.getUserEntityById(userId);
        Company company = companyService.getCompanyEntityById(companyId);

        CompanyMember member = CompanyMember.builder()
                .user(user)
                .company(company)
                .companyRole(CompanyRole.WORKER)
                .build();

        CompanyMember saved = companyMemberRepository.save(member);

        return CompanyMemberMapper.toResponse(saved);
    }

    //this is for the global admin , normal user shouldn't provide a company id
    @Override
    public void removeUserFromCompany(Long userId, Long companyId) {
        companyMemberRepository.findByUserIdAndCompanyId(userId, companyId)
                .ifPresent(companyMemberRepository::delete);
    }

    @Override
    public List<CompanyMemberResponse> getMembersByCompanyId(Long companyId) {
        return companyMemberRepository.findByCompanyId(companyId)
                .stream()
                .map(CompanyMemberMapper::toResponse)
                .toList();
    }

    @Override
    public CompanyMemberResponse getCompanyByUserId(Long userId) {
        return companyMemberRepository.findFirstByUserId(userId)
                .map(CompanyMemberMapper::toResponse)
                .orElse(null);
    }

    @Override
    public boolean isUserInCompany(Long userId, Long companyId) {
        return companyMemberRepository.existsByUserIdAndCompanyId(userId, companyId);
    }

    @Override
    public boolean isUserHeadInCompany(Long userId, Long companyId) {
        return companyMemberRepository.existsByUserIdAndCompanyIdAndCompanyRole(userId, companyId, CompanyRole.HEAD);
    }

    @Override
    public List<CompanyMemberResponse> getMyCompanyMembers() {
        Long userId = SecurityUtils.getCurrentUserId();
        CompanyMemberResponse membership = getCompanyByUserId(userId);
        if (membership == null) {
            throw new RuntimeException("User is not in this company");
        }
        if (membership.companyRole() != CompanyRole.HEAD) {
            throw new RuntimeException("Not allowed");
        }

        return companyMemberRepository
                .findByCompanyId(membership.company().id())
                .stream()
                .map(CompanyMemberMapper::toResponse)
                .toList();

    }

    @Override
    public CompanyMemberResponse updateRole(Long userId, Long companyId, CompanyRole companyRole) {

        CompanyMember membership = companyMemberRepository
                .findByUserIdAndCompanyId(userId, companyId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        membership.setCompanyRole(companyRole);

        CompanyMember saved = companyMemberRepository.save(membership);

        return CompanyMemberMapper.toResponse(saved);
    }


    @Override
    public void removeUserFromMyCompany(Long removedUserId) {

        Long userId = SecurityUtils.getCurrentUserId();

        CompanyMemberResponse membership = getCompanyByUserId(userId);

        if (membership == null) {
            throw new RuntimeException("User is not in this company");
        }
        if (membership.companyRole() != CompanyRole.HEAD) {
            throw new RuntimeException("Not allowed");
        }


        companyMemberRepository.findByUserIdAndCompanyId(removedUserId, membership.company().id())
                .ifPresent(companyMemberRepository::delete);
    }


}