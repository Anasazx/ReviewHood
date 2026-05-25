package tn.anasazx.tunirate.authentification.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.anasazx.tunirate.authentification.dto.AuthResponse;
import tn.anasazx.tunirate.authentification.dto.LoginRequest;
import tn.anasazx.tunirate.authentification.dto.RegisterRequest;
import tn.anasazx.tunirate.authentification.service.AuthService;
import tn.anasazx.tunirate.exception.InvalidPasswordException;
import tn.anasazx.tunirate.security.JwtService;
import tn.anasazx.tunirate.enums.GlobalRole;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.service.UserService;

@Service

@RequiredArgsConstructor

public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        
        //Check if the password valid or not
        if (!isStrongPassword(request.password())) {
            throw new InvalidPasswordException();
        }
        
        //Create user instance
        User user = new User(
                request.name(),
                request.email(), 
                passwordEncoder.encode(request.password()),
                GlobalRole.USER
        );

        //call the creation user methode 
        User savedUser = userService.createUser(user);

        //Generate token
        String token = jwtService.generateToken(savedUser.getId(), savedUser.getGlobalRole());

        return new AuthResponse(token, savedUser.getEmail(), savedUser.getGlobalRole().name());
    }

    private boolean isStrongPassword(String password) {
        return password != null
                && !password.isBlank()
                && password.length() >= 8;
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        //Find the user
        User user = userService.getUserByEmail(request.email());

        //Check password matching
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        //Token generation
        String token = jwtService.generateToken(user.getId(), user.getGlobalRole());
        return new AuthResponse(token, user.getEmail(), user.getGlobalRole().name());

    }

}