package tn.anasazx.tunirate.authentication.service.implementation;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.authentication.dto.*;
import tn.anasazx.tunirate.authentication.mapper.AuthMapper;
import tn.anasazx.tunirate.authentication.service.AuthService;
import tn.anasazx.tunirate.authentication.service.GoogleAuthService;
import tn.anasazx.tunirate.emailVerification.service.EmailVerificationService;
import tn.anasazx.tunirate.enums.AuthProvider;
import tn.anasazx.tunirate.enums.Country;
import tn.anasazx.tunirate.exception.InvalidPasswordException;
import tn.anasazx.tunirate.membership.entity.CompanyMember;
import tn.anasazx.tunirate.membership.repository.CompanyMemberRepository;
import tn.anasazx.tunirate.security.JwtService;
import tn.anasazx.tunirate.user.dto.MinimizedUserResponse;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.mapper.UserMapper;
import tn.anasazx.tunirate.user.repository.UserRepository;



@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final GoogleAuthService googleAuthService;
    private final EmailVerificationService emailVerificationService;
    private final CompanyMemberRepository companyMemberRepository;


    @Override
    public AuthResponse register(RegisterRequest request) {

        if (!isStrongPassword(request.password())) {
            throw new InvalidPasswordException();
        }

        if (userRepository.existsUserByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        
        //Create a user instance
        User user = new User(
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password())
        );

        if (request.country() != null) {
            user.setCountry(request.country());
        }

        User saved = userRepository.save(user);

        // Send verification code; the account already exists, so a mail failure must not fail the
        // registration (the user can ask for a new code via /email/request)
        try {
            emailVerificationService.requestVerificationCode(saved);
        } catch (RuntimeException e) {
            log.warn("Could not send verification email to user {}", saved.getId(), e);
        }

        //Generate token
        String token = jwtService.generateToken(saved);

        // A brand new user can't belong to a company yet
        return AuthMapper.toAuthResponse(token, saved, null);
    }

    private boolean isStrongPassword(String password) {
        return password != null
                && !password.isBlank()
                && password.length() >= 8;
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        //Find the user and check password matching. Same error for both cases so emails can't be enumerated.
        //Google-only accounts have no password and can't log in this way.
        User user = userRepository.findByEmail(request.email())
                .filter(u -> u.getPassword() != null
                        && request.password() != null
                        && passwordEncoder.matches(request.password(), u.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        //Token generation
        String token = jwtService.generateToken(user);

        return toAuthResponse(token, user);

    }


    @Override
    public MinimizedUserResponse authenticateUser(Long currentUserId) {

        //Find the user
        User user = userRepository.findFirstById(currentUserId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );

        return UserMapper.toMinimizedResponse(user);

    }



    @Override
    public AuthResponse googleLogin(GoogleLoginRequest request) {

        GoogleIdToken.Payload payload = googleAuthService.verifyToken(request.idToken());

        if (!payload.getEmailVerified()) {
            throw new RuntimeException("Google email is not verified");
        }

        String googleId = payload.getSubject();
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String picture = (String) payload.get("picture");

        User user = userRepository.findByGoogleId(googleId)
                .orElseGet(() -> userRepository.findByEmail(email)
                        .map(existingUser -> {

                            // Link Google account
                            existingUser.setGoogleId(googleId);
                            existingUser.setProvider(AuthProvider.GOOGLE);
                            existingUser.setEmailVerified(true);

                            return userRepository.save(existingUser);

                        })
                        .orElseGet(() -> {

                            User newUser = new User();

                            newUser.setEmail(email);
                            newUser.setName(name);
                            newUser.setAvatarUrl(picture);
                            newUser.setGoogleId(googleId);
                            newUser.setEmailVerified(true);
                            newUser.setCountry(Country.OTHER);
                            newUser.setProvider(AuthProvider.GOOGLE);

                            return userRepository.save(newUser);
                        }));

        String token = jwtService.generateToken(user);

        return toAuthResponse(token, user);
    }

    // Includes the company membership so the company portal knows the user's company and role
    private AuthResponse toAuthResponse(String token, User user) {
        CompanyMember membership = companyMemberRepository.findFirstByUserId(user.getId()).orElse(null);
        return AuthMapper.toAuthResponse(token, user, membership);
    }



}