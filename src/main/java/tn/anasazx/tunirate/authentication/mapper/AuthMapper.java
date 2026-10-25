package tn.anasazx.tunirate.authentication.mapper;

import tn.anasazx.tunirate.authentication.dto.AuthResponse;
import tn.anasazx.tunirate.membership.entity.CompanyMember;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.mapper.UserMapper;

public class AuthMapper {

    // membership may be null when the user doesn't belong to a company
    public static AuthResponse toAuthResponse(String token, User user, CompanyMember membership) {
        return new AuthResponse(
                token,
                UserMapper.toMinimizedResponse(user),
                user.getGlobalRole(),
                membership != null ? membership.getCompany().getId() : null,
                membership != null ? membership.getCompanyRole() : null
        );
    }

}
