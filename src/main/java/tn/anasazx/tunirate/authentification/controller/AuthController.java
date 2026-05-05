package tn.anasazx.tunirate.authentification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.anasazx.tunirate.authentification.dto.AuthResponse;
import tn.anasazx.tunirate.authentification.dto.LoginRequest;
import tn.anasazx.tunirate.authentification.dto.RegisterRequest;
import tn.anasazx.tunirate.authentification.service.AuthService;

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

}