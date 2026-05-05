package tn.anasazx.tunirate.authentification.service;

import tn.anasazx.tunirate.authentification.dto.AuthResponse;
import tn.anasazx.tunirate.authentification.dto.LoginRequest;
import tn.anasazx.tunirate.authentification.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

}