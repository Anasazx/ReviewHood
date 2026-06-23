package tn.anasazx.tunirate.authentication.dto;

public record RegisterRequest(
        String name,
        String email,
        String password
) {}