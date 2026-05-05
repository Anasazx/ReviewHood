package tn.anasazx.tunirate.authentification.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.anasazx.tunirate.authentification.dto.AuthResponse;
import tn.anasazx.tunirate.authentification.dto.LoginRequest;
import tn.anasazx.tunirate.authentification.dto.RegisterRequest;
import tn.anasazx.tunirate.authentification.service.AuthService;
import tn.anasazx.tunirate.security.JwtService;
import tn.anasazx.tunirate.user.Role;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.repository.UserRepository;

@Service

@RequiredArgsConstructor

public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Override

    public AuthResponse register(RegisterRequest request) {

        // 1. check if user exists

        if (userRepository.findByEmail(request.email()).isPresent()) {

            throw new RuntimeException("Email already exists");

        }

        // 2. create user

        User user = new User();

        user.setName(request.name());

        user.setEmail(request.email());

        user.setPassword(passwordEncoder.encode(request.password()));

        // 3. role

        user.setRole(Role.valueOf(request.role()));

        userRepository.save(user);

        // 4. generate token

        String token = jwtService.generateToken(user.getId());

        return new AuthResponse(token, user.getEmail(), user.getRole().name());

    }

    @Override

    public AuthResponse login(LoginRequest request) {

        // 1. find user

        User user = userRepository.findByEmail(request.email())

                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. check password

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {

            throw new RuntimeException("Invalid credentials");

        }

        // 3. generate token

        String token = jwtService.generateToken(user.getId());

        return new AuthResponse(token, user.getEmail(), user.getRole().name());

    }

}