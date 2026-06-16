package tn.anasazx.tunirate.invitation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.enums.InvitationStatus;
import tn.anasazx.tunirate.invitation.entity.CompanyInvitation;
import tn.anasazx.tunirate.user.entity.User;

import java.util.List;

public interface CompanyInvitationRepository extends JpaRepository<CompanyInvitation, Long> {

    boolean existsByCompanyAndUser(Company company, User user);

    boolean existsByCompanyAndUserAndStatusIn(Company company, User user, List<InvitationStatus> listOfInvitationStatus);

    List<CompanyInvitation> findAllByCompanyId(Long companyId);

    List<CompanyInvitation> findAllByUserIdAndStatusInOrderByCreatedAtDesc(Long userId, List<InvitationStatus> listOfInvitationStatus);

}
