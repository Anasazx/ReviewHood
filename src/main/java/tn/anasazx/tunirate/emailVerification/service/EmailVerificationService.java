package tn.anasazx.tunirate.emailVerification.service;

import tn.anasazx.tunirate.emailVerification.dto.VerifyEmailRequest;
import tn.anasazx.tunirate.user.entity.User;

public interface EmailVerificationService {
    void requestVerificationCode();
    void requestVerificationCode(User user);
    void verifyEmail(VerifyEmailRequest request);
}
