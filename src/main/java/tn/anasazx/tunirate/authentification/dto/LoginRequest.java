package tn.anasazx.tunirate.authentification.dto;


public record LoginRequest(
        String email,
        String password
) {}