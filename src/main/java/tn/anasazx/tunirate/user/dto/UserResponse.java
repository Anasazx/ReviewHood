package tn.anasazx.tunirate.user.dto;

import tn.anasazx.tunirate.enums.Country;
import tn.anasazx.tunirate.enums.UserStatus;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String name,
        String email,
        boolean emailVerified,
        String avatarUrl,
        String phoneNumber,
        Country country,
        UserStatus status,
        LocalDateTime createdAt
) {}