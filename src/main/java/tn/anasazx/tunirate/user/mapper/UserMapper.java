package tn.anasazx.tunirate.user.mapper;

import tn.anasazx.tunirate.user.dto.MinimizedUserResponse;
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
                user.getCreatedAt()
        );
    }

    public static MinimizedUserResponse toAuthResponse(User user) {
        return new MinimizedUserResponse(
                user.getId(),
                user.getName(),
                user.getAvatarUrl()
        );
    }

    public static MinimizedUserResponse toAuthResponse(UserResponse user) {
        return new MinimizedUserResponse(
                user.id(),
                user.name(),
                user.avatarUrl()
        );
    }

}