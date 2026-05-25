package tn.anasazx.tunirate.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import tn.anasazx.tunirate.enums.GlobalRole;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    //TODO: move this to application.properties
    private static final String SECRET =
            "THIS_IS_A_SUPER_LONG_SECRET_KEY_FOR_TUNIRATE_APPLICATION_123456789";

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    //Generate token
    public String generateToken(Long id, GlobalRole globalRole) {
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("role", globalRole.name())
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

    //TODO: IMO this is a big security gap to get the user's role from the token
    // so in the future it needs to this workflow :
    //      if user is requesting a gateway that is protected by admin privilege it gets the id from the token then it runs a query
    //      to check if this user has admin privileges



    //Extract role
    public String extractRole(String token) {
        return parseClaims(token)
                .getBody()
                .get("role", String.class);
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