package tn.anasazx.tunirate.authentification.dto;

public record AuthResponse(
        String token,
        String email,
        String role
) {}