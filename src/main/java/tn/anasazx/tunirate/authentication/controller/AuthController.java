package tn.anasazx.tunirate.authentication.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.anasazx.tunirate.authentication.dto.*;
import tn.anasazx.tunirate.authentication.service.AuthService;
import tn.anasazx.tunirate.user.dto.MinimizedUserResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleLogin(@RequestBody GoogleLoginRequest request) {
        return ResponseEntity.ok(
                authService.googleLogin(request)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<MinimizedUserResponse> authenticateUser() {
        return ResponseEntity.ok(authService.authenticateUser());
    }

}