package tn.anasazx.tunirate.emailVerification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.anasazx.tunirate.emailVerification.entity.EmailVerification;
import tn.anasazx.tunirate.user.entity.User;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    Optional<EmailVerification> findByUser(User user);

}