package tn.anasazx.tunirate.authentification.dto;

public record RegisterRequest(

        String name,

        String email,

        String password,

        String role

) {}