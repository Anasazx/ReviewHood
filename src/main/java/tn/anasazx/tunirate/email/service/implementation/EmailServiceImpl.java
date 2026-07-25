package tn.anasazx.tunirate.email.service.implementation;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tn.anasazx.tunirate.email.service.EmailService;
import tn.anasazx.tunirate.email.service.EmailTemplateService;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    private final EmailTemplateService emailTemplateService;

    @Value("${spring.mail.username}")
    private String from;

    private void sendEmail(String to, String subject, String body) {

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);

            // HTML email
            helper.setText(body, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }

    }

    @Override
    public void sendVerificationEmail(String to, String code) {
        String body = emailTemplateService.verificationEmail(String.valueOf(code));
        sendEmail(to, "Verify your ReviewHood account", body);
    }

    //TODO: To complete this method
    @Override
    public void sendPasswordResetEmail(String to, String code){
        String body = "";
        sendEmail(to, "Reset your ReviewHood account", body);
    }

    //TODO: To complete this method
    @Override
    public void sendWelcomeEmail(String to, String name){
        String body = "";
        sendEmail(to, "Welcome to ReviewHood", body);
    }

}
