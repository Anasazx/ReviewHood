package tn.anasazx.tunirate.membership.dto;

import tn.anasazx.tunirate.company.dto.CompanyResponse;
import tn.anasazx.tunirate.enums.CompanyRole;
import tn.anasazx.tunirate.user.dto.UserResponseDto;


import java.time.LocalDateTime;

public record CompanyMemberResponse (
        UserResponseDto user,
        CompanyResponse company,
        CompanyRole companyRole,
        LocalDateTime joinedAt
){}


