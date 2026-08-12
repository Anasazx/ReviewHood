package tn.anasazx.tunirate.user.mapper;

import tn.anasazx.tunirate.user.dto.MinimizedUserResponse;
import tn.anasazx.tunirate.user.dto.SecureMinimizedUserResponse;
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

    public static MinimizedUserResponse toMinimizedResponse(User user) {
        return new MinimizedUserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.isEmailVerified(),
                user.getAvatarUrl()
        );
    }

    public static SecureMinimizedUserResponse toSecureMinimizedUserResponse(User user) {
        return new SecureMinimizedUserResponse(
                user.getId(),
                user.getName(),
                user.getAvatarUrl()
        );
    }

    public static MinimizedUserResponse toMinimizedResponse(UserResponse user) {
        return new MinimizedUserResponse(
                user.id(),
                user.name(),
                user.email(),
                user.emailVerified(),
                user.avatarUrl()
        );
    }

}