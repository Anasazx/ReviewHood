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
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyMemberServiceImpl implements CompanyMemberService {

    private final CompanyMemberRepository companyMemberRepository;
    private final UserService userService;
    private final CompanyService companyService;



    @Override
    public CompanyMemberResponse assignUserToCompany(Long userId, Long companyId, CompanyRole role) {

        if (companyMemberRepository.existsByUserIdAndCompanyId(userId, companyId)) {
            throw new RuntimeException("User already in this company");
        }

        User user = userService.getUserEntityById(userId);
        Company company = companyService.getCompanyEntityById(companyId);

        CompanyMember member = CompanyMember.builder()
                .user(user)
                .company(company)
                .role(role)
                .build();

        CompanyMember saved = companyMemberRepository.save(member);

        return CompanyMemberMapper.toResponse(saved);
    }

    @Override
    public void removeUserFromCompany(Long userId, Long companyId) {
        companyMemberRepository.findByUserIdAndCompanyId(userId, companyId)
                .ifPresent(companyMemberRepository::delete);
    }

    @Override
    public List<CompanyMemberResponse> getMembersByCompany(Long companyId) {
        return companyMemberRepository.findByCompanyId(companyId)
                .stream()
                .map(CompanyMemberMapper::toResponse)
                .toList();
    }

    @Override

    public List<CompanyMemberResponse> getCompaniesByUser(Long userId) {
        return companyMemberRepository.findByUserId(userId)
                .stream()
                .map(CompanyMemberMapper::toResponse)
                .toList();
    }

    @Override
    public boolean isUserInCompany(Long userId, Long companyId) {
        return companyMemberRepository.existsByUserIdAndCompanyId(userId, companyId);
    }

    @Override

    public CompanyMemberResponse updateRole(Long userId, Long companyId, CompanyRole role) {

        CompanyMember membership = companyMemberRepository
                .findByUserIdAndCompanyId(userId, companyId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        membership.setRole(role);
        CompanyMember saved = companyMemberRepository.save(membership);

        return CompanyMemberMapper.toResponse(saved);
    }


}