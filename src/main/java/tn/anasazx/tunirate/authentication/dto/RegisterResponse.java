package tn.anasazx.tunirate.authentication.dto;

import tn.anasazx.tunirate.user.dto.AuthUserDTO;

public record RegisterResponse(
        String token,
        AuthUserDTO user
) {}