package tn.anasazx.tunirate.user.dto;

import tn.anasazx.tunirate.enums.GlobalRole;

// Every field is optional, only the provided ones are updated
public record AdminUpdateUserRequest(
        String name,
        GlobalRole globalRole
) {}
