package tn.anasazx.tunirate.user.dto;

import tn.anasazx.tunirate.enums.Country;

public record updateUserRequest(
        String name,
        String email,
        boolean emailVerified,
        String password,
        String avatarUrl,
        String phoneNumber,
        Country country
) {}