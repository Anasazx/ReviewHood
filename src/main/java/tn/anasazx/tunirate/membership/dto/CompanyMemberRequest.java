package tn.anasazx.tunirate.membership.dto;

import jakarta.validation.constraints.NotNull;
import tn.anasazx.tunirate.enums.CompanyRole;

public record CompanyMemberRequest(
        @NotNull Long userId,
        @NotNull Long companyId
) {}
