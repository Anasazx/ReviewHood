package tn.anasazx.tunirate.user.dto;

import tn.anasazx.tunirate.enums.AuthProvider;
import tn.anasazx.tunirate.enums.CompanyRole;
import tn.anasazx.tunirate.enums.GlobalRole;
import tn.anasazx.tunirate.enums.UserStatus;

import java.time.LocalDateTime;

// One row of the admin users table, including the user's company membership (null if none)
public record AdminUserResponse(
        Long id,
        String name,
        String email,
        String avatarUrl,
        boolean emailVerified,
        GlobalRole globalRole,
        UserStatus status,
        AuthProvider provider,
        Long companyId,
        String companyName,
        CompanyRole companyRole,
        LocalDateTime createdAt
) {}
