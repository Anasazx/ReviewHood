package tn.anasazx.tunirate.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;


import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    //TODO: move this to application.properties
    private static final String SECRET =
            "THIS_IS_A_SUPER_LONG_SECRET_KEY_FOR_TUNIRATE_APPLICATION_123456789";

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    //Generate token
    public String generateToken(Long id) {
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }




    //Extract userId
    public Long extractUserId(String token) {
        return Long.parseLong(
                parseClaims(token).getBody().getSubject()
        );
    }



    //Validate token
    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    //Check expiration
    private boolean isTokenExpired(String token) {
        return parseClaims(token).getBody().getExpiration().before(new Date());
    }



    //Parse token safely
    private Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}