package tn.anasazx.tunirate.authentication.mapper;

import tn.anasazx.tunirate.authentication.dto.AuthResponse;
import tn.anasazx.tunirate.user.dto.UserResponse;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.mapper.UserMapper;

public class AuthMapper {

    public static AuthResponse toAuthResponse(String token, User user) {
        return new AuthResponse(
                token,
                UserMapper.toAuthResponse(user)
        );
    }

    public static AuthResponse toAuthResponse(String token, UserResponse user) {
        return new AuthResponse(
                token,
                UserMapper.toAuthResponse(user)
        );
    }

}
