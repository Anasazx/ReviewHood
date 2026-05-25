package tn.anasazx.tunirate.user.mapper;

import tn.anasazx.tunirate.user.dto.UserResponseDto;
import tn.anasazx.tunirate.user.entity.User;

public class UserMapper {

    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getGlobalRole(),
                user.getCreatedAt()
        );
    }
}
