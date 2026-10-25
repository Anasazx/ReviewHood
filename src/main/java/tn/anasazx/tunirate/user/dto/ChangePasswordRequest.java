package tn.anasazx.tunirate.user.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
        // not needed for Google accounts that never had a password
        String currentPassword,
        @NotBlank String newPassword
) {}
