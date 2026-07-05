package tn.anasazx.tunirate.authentication.dto;

import tn.anasazx.tunirate.enums.Country;

public record RegisterRequest(
        String name,
        String email,
        Country country,
        String password
) {}