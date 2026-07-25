package tn.anasazx.tunirate.emailVerification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.anasazx.tunirate.emailVerification.dto.VerifyEmailRequest;
import tn.anasazx.tunirate.emailVerification.service.EmailVerificationService;

import java.util.Map;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/request")
    public ResponseEntity<?> requestVerificationCode() {
        emailVerificationService.requestVerificationCode();
        return ResponseEntity.ok(
                Map.of("message", "Verification code sent successfully" )
        );
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody VerifyEmailRequest request) {
        emailVerificationService.verifyEmail(request);
        return ResponseEntity.ok(
                Map.of("message", "Email verified successfully")
        );
    }

}