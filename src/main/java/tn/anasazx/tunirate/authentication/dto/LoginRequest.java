package tn.anasazx.tunirate.authentication.dto;


public record LoginRequest(
        String email,
        String password
) {}