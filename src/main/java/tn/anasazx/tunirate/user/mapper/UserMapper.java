package tn.anasazx.tunirate.user.mapper;

import tn.anasazx.tunirate.user.dto.UserResponse;
import tn.anasazx.tunirate.user.entity.User;

public class UserMapper {

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getGlobalRole(),
                user.getCreatedAt()
        );
    }
}
