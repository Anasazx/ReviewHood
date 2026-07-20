package tn.anasazx.tunirate.authentication.service.implementation;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.authentication.dto.*;
import tn.anasazx.tunirate.authentication.mapper.AuthMapper;
import tn.anasazx.tunirate.authentication.service.AuthService;
import tn.anasazx.tunirate.authentication.service.GoogleAuthService;
import tn.anasazx.tunirate.enums.AuthProvider;
import tn.anasazx.tunirate.enums.Country;
import tn.anasazx.tunirate.exception.InvalidPasswordException;
import tn.anasazx.tunirate.security.JwtService;
import tn.anasazx.tunirate.security.SecurityUtils;
import tn.anasazx.tunirate.user.dto.MinimizedUserResponse;
import tn.anasazx.tunirate.user.dto.UserResponse;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.mapper.UserMapper;
import tn.anasazx.tunirate.user.repository.UserRepository;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final GoogleAuthService googleAuthService;


    @Override
    public AuthResponse register(RegisterRequest request) {
        
        //Check if the password valid or not
        if (!isStrongPassword(request.password())) {
            throw new InvalidPasswordException();
        }

        if (userRepository.existsUserByEmailOrName(request.email(), request.name())) {
            throw new RuntimeException("Email or name already exists");
        }
        
        //Create a user instance
        User user = new User(
                request.name(),
                request.email(),
                request.country(),
                passwordEncoder.encode(request.password())
        );

        UserResponse savedUser = UserMapper.toResponse(userRepository.save(user));

        //Generate token
        String token = jwtService.generateToken(savedUser.id());

        return AuthMapper.toAuthResponse(token, savedUser);
    }

    private boolean isStrongPassword(String password) {
        return password != null
                && !password.isBlank()
                && password.length() >= 8;
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        //Find the user
        User user = userRepository.findByEmail(request.email()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );

        //Check password matching
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        //Token generation
        String token = jwtService.generateToken(user.getId());

        return AuthMapper.toAuthResponse(token, user);

    }


    @Override
    public MinimizedUserResponse authenticateUser() {

        Long currentUserId = SecurityUtils.getCurrentUserId();

        //Find the user
        User user = userRepository.findFirstById(currentUserId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );

        return UserMapper.toMinimizedResponse(user);

    }



    @Override
    public AuthResponse googleLogin(GoogleLoginRequest request) {

        GoogleIdToken.Payload payload = googleAuthService.verifyToken(request.idToken());


        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String picture = (String) payload.get("picture");

        User user = userRepository.findByEmail(email)

                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setAvatarUrl(picture);
                    newUser.setEmailVerified(true);
                    newUser.setPassword(
                            passwordEncoder.encode(UUID.randomUUID().toString())
                    );
                    newUser.setCountry(Country.OTHER);
                    newUser.setProvider(AuthProvider.GOOGLE);
                    return userRepository.save(newUser);

                });

        String token = jwtService.generateToken(user.getId());

        return AuthMapper.toAuthResponse(token, user);
    }



}