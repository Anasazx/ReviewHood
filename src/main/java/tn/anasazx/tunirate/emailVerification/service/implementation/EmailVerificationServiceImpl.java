package tn.anasazx.tunirate.emailVerification.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.anasazx.tunirate.email.service.EmailService;
import tn.anasazx.tunirate.emailVerification.dto.VerifyEmailRequest;
import tn.anasazx.tunirate.emailVerification.entity.EmailVerification;
import tn.anasazx.tunirate.emailVerification.repository.EmailVerificationRepository;
import tn.anasazx.tunirate.emailVerification.service.EmailVerificationService;
import tn.anasazx.tunirate.security.SecurityUtils;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.repository.UserRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void requestVerificationCode() {
        User user = getCurrentUser();
        requestVerificationCode(user);
    }

    @Transactional
    @Override
    public void requestVerificationCode(User user) {

        if (user.isEmailVerified()) {
            throw new RuntimeException("Email already verified");
        }

        EmailVerification verification = emailVerificationRepository.findByUser(user).orElse(null);

        String code = generateCode();

        if (verification == null) {

            EmailVerification newVerification = EmailVerification.builder()
                    .user(user)
                    .codeHash(encoder.encode(code))
                    .build();

            emailVerificationRepository.save(newVerification);
            emailService.sendVerificationEmail(user.getEmail(), code);


        }
        else {

            if (verification.getResendResetAt().isBefore(LocalDateTime.now())) {
                verification.setResendCount(0);
                verification.setResendResetAt(LocalDateTime.now().plusDays(1));
            }

            if (verification.getResendCount() >= 5 || verification.getLastSentAt().plusSeconds(30).isAfter(LocalDateTime.now())) {
                throw new RuntimeException("Please wait before requesting another code");
            }

            verification.setResendCount(verification.getResendCount() + 1);
            verification.setLastSentAt(LocalDateTime.now());
            verification.setAttempts(0);
            verification.setCodeHash(encoder.encode(code));
            verification.setExpiresAt(LocalDateTime.now().plusMinutes(10));
            emailVerificationRepository.save(verification);
            emailService.sendVerificationEmail(user.getEmail(), code);

        }

    }

    @Transactional
    @Override
    public void verifyEmail(VerifyEmailRequest request) {

        User user = getCurrentUser();

        if (user.isEmailVerified()) {
            throw new RuntimeException("Email already verified");
        }

        EmailVerification verification = emailVerificationRepository.findByUser(user)
                        .orElseThrow(() -> new RuntimeException("Verification code not found"));

        if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification code expired");
        }

        if (verification.getAttempts() >= 10) {
            throw new RuntimeException("Too many verification attempts. Please try again later.");
        }

        if (!encoder.matches(request.code(), verification.getCodeHash())) {
            verification.setAttempts(verification.getAttempts() + 1);
            emailVerificationRepository.save(verification);
            throw new RuntimeException("Invalid verification code");
        }

        user.setEmailVerified(true);
        userRepository.save(user);
        emailVerificationRepository.delete(verification);

    }

    private User getCurrentUser() {
        Long userId = SecurityUtils.getCurrentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private String generateCode() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1000000));
    }

}