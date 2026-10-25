package tn.anasazx.tunirate.authentication.dto;

import tn.anasazx.tunirate.enums.CompanyRole;
import tn.anasazx.tunirate.enums.GlobalRole;
import tn.anasazx.tunirate.user.dto.MinimizedUserResponse;

public record AuthResponse(
        String token,
        MinimizedUserResponse user,
        GlobalRole role,
        // null when the user doesn't belong to a company
        Long companyId,
        CompanyRole companyRole
) {}
