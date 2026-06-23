package tn.anasazx.tunirate.authentication.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.authentication.dto.AuthResponse;
import tn.anasazx.tunirate.authentication.dto.LoginRequest;
import tn.anasazx.tunirate.authentication.dto.RegisterRequest;
import tn.anasazx.tunirate.authentication.service.AuthService;
import tn.anasazx.tunirate.enums.CompanyRole;
import tn.anasazx.tunirate.exception.InvalidPasswordException;
import tn.anasazx.tunirate.membership.dto.CompanyMemberResponse;
import tn.anasazx.tunirate.membership.service.CompanyMemberService;
import tn.anasazx.tunirate.security.JwtService;
import tn.anasazx.tunirate.enums.GlobalRole;
import tn.anasazx.tunirate.user.dto.UserResponse;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.repository.UserRepository;
import tn.anasazx.tunirate.user.service.UserService;


@Service

@RequiredArgsConstructor

public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserService userService;
    private final CompanyMemberService companyMemberService;
    private final UserRepository userRepository;


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
        UserResponse savedUser = userService.createUser(user);

        //Generate token
        String token = jwtService.generateToken(savedUser.id());

        return new AuthResponse(token, savedUser.email(), savedUser.globalRole().name(), null, null);
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


        CompanyMemberResponse userCompanyMembership = companyMemberService.getCompanyByUserId(user.getId());

        Long companyId = null;
        CompanyRole companyRole = null;

        if (userCompanyMembership != null) {
            companyId = userCompanyMembership.company().id();
            companyRole = userCompanyMembership.companyRole();
        }

        //Token generation
        String token = jwtService.generateToken(user.getId());
        return new AuthResponse(token, user.getEmail(), user.getGlobalRole().name(), companyId, companyRole);
    }

}