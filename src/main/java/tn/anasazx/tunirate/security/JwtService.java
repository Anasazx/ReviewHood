package tn.anasazx.tunirate.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tn.anasazx.tunirate.user.entity.User;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final Key key;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }


    // Generate token
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("role", user.getGlobalRole().name())
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)
                ) // 24h
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    // Extract userId
    public Long extractUserId(String token) {
        return Long.parseLong(
                parseClaims(token).getBody().getSubject()
        );
    }

    // Extract userRole
    public String extractUserRole(String token) {
        return parseClaims(token)
                .getBody()
                .get("role", String.class);
    }

    // Validate token
    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return !isTokenExpired(token);

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


    // Check expiration
    private boolean isTokenExpired(String token) {
        return parseClaims(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }


    // Parse token safely
    private Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}