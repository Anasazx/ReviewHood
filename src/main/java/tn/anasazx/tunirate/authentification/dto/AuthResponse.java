package tn.anasazx.tunirate.authentification.dto;

import tn.anasazx.tunirate.enums.CompanyRole;

public record AuthResponse(
        String token,
        String email,
        String role,
        Long companyId,
        CompanyRole companyRole
) {}