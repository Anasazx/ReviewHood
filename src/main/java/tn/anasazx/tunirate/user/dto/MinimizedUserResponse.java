package tn.anasazx.tunirate.user.dto;

public record MinimizedUserResponse(
        Long id,
        String name,
        String email,
        boolean emailVerified,
        String avatarUrl
) {}