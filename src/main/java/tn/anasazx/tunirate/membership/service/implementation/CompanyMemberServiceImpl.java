package tn.anasazx.tunirate.membership.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.company.repository.CompanyRepository;
import tn.anasazx.tunirate.enums.CompanyRole;
import tn.anasazx.tunirate.membership.entity.CompanyMember;
import tn.anasazx.tunirate.membership.repository.CompanyMemberRepository;
import tn.anasazx.tunirate.membership.service.CompanyMemberService;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyMemberServiceImpl implements CompanyMemberService {

    private final CompanyMemberRepository repository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Override
    public CompanyMember assignUserToCompany(Long userId, Long companyId, CompanyRole role) {
        if (repository.existsByUserIdAndCompanyId(userId, companyId)) {
            throw new RuntimeException("User already in this company");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        CompanyMember member = CompanyMember.builder()
                .user(user)
                .company(company)
                .role(role)
                .build();
        return repository.save(member);
    }

    @Override
    public void removeUserFromCompany(Long userId, Long companyId) {
        repository.findByUserIdAndCompanyId(userId, companyId)
                .ifPresent(repository::delete);
    }

    @Override
    public List<CompanyMember> getMembersByCompany(Long companyId) {
        return repository.findByCompanyId(companyId);
    }

    @Override
    public List<CompanyMember> getCompaniesByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public boolean isUserInCompany(Long userId, Long companyId) {
        return repository.existsByUserIdAndCompanyId(userId, companyId);
    }

}