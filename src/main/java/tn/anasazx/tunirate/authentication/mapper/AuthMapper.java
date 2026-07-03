package tn.anasazx.tunirate.authentication.mapper;

import tn.anasazx.tunirate.authentication.dto.LoginResponse;
import tn.anasazx.tunirate.authentication.dto.RegisterResponse;

import tn.anasazx.tunirate.user.dto.UserResponse;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.mapper.UserMapper;

public class AuthMapper {

    public static LoginResponse toLoginResponse(String token, User user) {
        return new LoginResponse(
                token,
                UserMapper.toAuthResponse(user)
        );
    }

    public static RegisterResponse toRegisterResponse(String token, UserResponse user) {
        return new RegisterResponse(
                token,
                UserMapper.toAuthResponse(user)
        );
    }

}
