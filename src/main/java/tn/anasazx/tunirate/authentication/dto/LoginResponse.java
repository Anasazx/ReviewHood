package tn.anasazx.tunirate.authentication.dto;

import tn.anasazx.tunirate.user.dto.AuthUserDTO;

public record LoginResponse(
        String token,
        AuthUserDTO user
) {}