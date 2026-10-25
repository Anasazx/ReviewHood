package tn.anasazx.tunirate.membership.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.company.repository.CompanyRepository;
import tn.anasazx.tunirate.enums.CompanyRole;
import tn.anasazx.tunirate.membership.dto.CompanyMemberResponse;
import tn.anasazx.tunirate.membership.entity.CompanyMember;
import tn.anasazx.tunirate.membership.mapper.CompanyMemberMapper;
import tn.anasazx.tunirate.membership.repository.CompanyMemberRepository;
import tn.anasazx.tunirate.membership.service.CompanyMemberService;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyMemberServiceImpl implements CompanyMemberService {

    private final CompanyMemberRepository companyMemberRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final CompanyMemberMapper companyMemberMapper;

    @Override
    public CompanyMemberResponse assignUserToCompany(Long userId, Long companyId) {
        return assignUserToCompany(userId, companyId, CompanyRole.WORKER);
    }

    @Override
    public CompanyMemberResponse assignUserToCompany(Long userId, Long companyId, CompanyRole companyRole) {

        // The app works with one company per user (see findFirstByUserId usages)
        companyMemberRepository.findFirstByUserId(userId).ifPresent(existing -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "User already belongs to " + existing.getCompany().getName());
        });

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));


        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company Not Found"));

        CompanyMember member = CompanyMember.builder()
                .user(user)
                .company(company)
                .companyRole(companyRole != null ? companyRole : CompanyRole.WORKER)
                .build();

        CompanyMember saved = companyMemberRepository.save(member);

        return companyMemberMapper.toResponse(saved);
    }

    //this is for the global admin, normal user shouldn't provide a company id
    @Override
    public void removeUserFromCompany(Long userId, Long companyId) {
        companyMemberRepository.findByUserIdAndCompanyId(userId, companyId)
                .ifPresent(companyMemberRepository::delete);
    }

    @Override
    public List<CompanyMemberResponse> getMembersByCompanyId(Long companyId) {
        return companyMemberRepository.findByCompanyId(companyId)
                .stream()
                .map(companyMemberMapper::toResponse)
                .toList();
    }

    @Override
    public CompanyMemberResponse getCompanyByUserId(Long userId) {
        return companyMemberRepository.findFirstByUserId(userId)
                .map(companyMemberMapper::toResponse)
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
    public CompanyMemberResponse updateRoleInMyCompany(Long userId, CompanyRole companyRole, Long currentUserId) {

        CompanyMemberResponse membership = requireHead(currentUserId);

        if (userId.equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't change your own role");
        }

        return updateRole(userId, membership.companyId(), companyRole);
    }

    private CompanyMemberResponse requireHead(Long currentUserId) {
        CompanyMemberResponse membership = getCompanyByUserId(currentUserId);
        if (membership == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't belong to any company");
        }
        if (membership.companyRole() != CompanyRole.HEAD) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the company head can manage the team");
        }
        return membership;
    }

    @Override
    public List<CompanyMemberResponse> getMyCompanyMembers(Long currentUserId) {
        CompanyMemberResponse membership = requireHead(currentUserId);

        return companyMemberRepository
                .findByCompanyId(membership.companyId())
                .stream()
                .map(companyMemberMapper::toResponse)
                .toList();

    }

    @Override
    public CompanyMemberResponse updateRole(Long userId, Long companyId, CompanyRole companyRole) {

        CompanyMember membership = companyMemberRepository
                .findByUserIdAndCompanyId(userId, companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

        membership.setCompanyRole(companyRole);

        CompanyMember saved = companyMemberRepository.save(membership);

        return companyMemberMapper.toResponse(saved);
    }


    @Override
    public void removeUserFromMyCompany(Long removedUserId, Long currentUserId) {

        CompanyMemberResponse membership = requireHead(currentUserId);

        if (removedUserId.equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't remove yourself from the company");
        }


        companyMemberRepository.findByUserIdAndCompanyId(removedUserId, membership.companyId())
                .ifPresent(companyMemberRepository::delete);
    }


}