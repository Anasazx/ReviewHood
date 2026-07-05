package tn.anasazx.tunirate.user.dto;

import tn.anasazx.tunirate.enums.Country;

public record UpdateUserRequest(
        String email,
        String phoneNumber,
        Country country //Not for now, in futur
) {}