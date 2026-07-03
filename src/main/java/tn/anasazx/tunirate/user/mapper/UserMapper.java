package tn.anasazx.tunirate.user.mapper;

import tn.anasazx.tunirate.user.dto.AuthUserDTO;
import tn.anasazx.tunirate.user.dto.UserResponse;
import tn.anasazx.tunirate.user.entity.User;

public class UserMapper {

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.isEmailVerified(),
                user.getAvatarUrl(),
                user.getPhoneNumber(),
                user.getCountry(),
                user.getStatus(),
                user.getGlobalRole(),
                user.getCreatedAt()
        );
    }

    public static AuthUserDTO toAuthResponse(User user) {
        return new AuthUserDTO(
                user.getId(),
                user.getName(),
                user.getAvatarUrl()
        );
    }

    public static AuthUserDTO toAuthResponse(UserResponse user) {
        return new AuthUserDTO(
                user.id(),
                user.name(),
                user.avatarUrl()
        );
    }

}