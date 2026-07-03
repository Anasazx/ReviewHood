package tn.anasazx.tunirate.authentication.service;


import tn.anasazx.tunirate.authentication.dto.LoginRequest;
import tn.anasazx.tunirate.authentication.dto.LoginResponse;
import tn.anasazx.tunirate.authentication.dto.RegisterRequest;
import tn.anasazx.tunirate.authentication.dto.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}