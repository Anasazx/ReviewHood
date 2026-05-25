package tn.anasazx.tunirate.user.dto;

import tn.anasazx.tunirate.enums.GlobalRole;

import java.time.LocalDateTime;

public record UserResponseDto(
        Long id,
        String name,
        String email,
        GlobalRole globalRole,
        LocalDateTime createdAt
) {}
