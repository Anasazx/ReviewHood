package tn.anasazx.tunirate.authentication.dto;

import tn.anasazx.tunirate.enums.CompanyRole;

public record AuthResponse(
        String token,
        String username,
        String email,
        String role,
        Long companyId,
        CompanyRole companyRole
) {}