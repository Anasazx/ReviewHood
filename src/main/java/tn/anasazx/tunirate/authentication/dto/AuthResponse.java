package tn.anasazx.tunirate.authentication.dto;

import tn.anasazx.tunirate.user.dto.MinimizedUserResponse;

public record AuthResponse(
        String token,
        MinimizedUserResponse user
) {}
