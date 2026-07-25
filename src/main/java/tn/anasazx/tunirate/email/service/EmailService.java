package tn.anasazx.tunirate.email.service;

public interface EmailService {
    void sendVerificationEmail(String to, String code);
    void sendPasswordResetEmail(String to, String code);
    void sendWelcomeEmail(String to, String name);
}
