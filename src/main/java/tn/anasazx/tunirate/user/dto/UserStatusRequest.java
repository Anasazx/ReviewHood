package tn.anasazx.tunirate.user.dto;

import jakarta.validation.constraints.NotNull;
import tn.anasazx.tunirate.enums.UserStatus;

public record UserStatusRequest(
        @NotNull UserStatus status
) {}
