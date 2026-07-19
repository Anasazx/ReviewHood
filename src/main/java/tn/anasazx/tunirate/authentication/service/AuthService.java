package tn.anasazx.tunirate.authentication.service;


import tn.anasazx.tunirate.authentication.dto.AuthResponse;
import tn.anasazx.tunirate.authentication.dto.GoogleLoginRequest;
import tn.anasazx.tunirate.authentication.dto.LoginRequest;
import tn.anasazx.tunirate.authentication.dto.RegisterRequest;
import tn.anasazx.tunirate.user.dto.MinimizedUserResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    MinimizedUserResponse authenticateUser ();
    AuthResponse googleLogin(GoogleLoginRequest request);
}