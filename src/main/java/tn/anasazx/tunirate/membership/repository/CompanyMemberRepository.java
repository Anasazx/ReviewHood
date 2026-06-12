package tn.anasazx.tunirate.membership.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.anasazx.tunirate.enums.CompanyRole;
import tn.anasazx.tunirate.membership.entity.CompanyMember;

import java.util.List;
import java.util.Optional;


public interface CompanyMemberRepository extends JpaRepository<CompanyMember, Long> {

    // Find all members of a company
    List<CompanyMember> findByCompanyId(Long companyId);

    // Find company of a user
    Optional<CompanyMember> findFirstByUserId(Long userId);

    // Check if user is already in company
    boolean existsByUserIdAndCompanyId(Long userId, Long companyId);

    // Get specific membership (useful for role checks)
    Optional<CompanyMember> findByUserIdAndCompanyId(Long userId, Long companyId);

    // Get all HEADS of a company
    List<CompanyMember> findByCompanyIdAndRole(Long companyId, CompanyRole role);

}