package com.project.CourseManagement.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JWTUtils {
    private static final String SECRET =
            "sibasis-badatya-secret-key-256-bit-long-123456";

    private final SecretKey jwtKey =
            Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(jwtKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUserNameFromToken(String token) {
        try {
            String subject = extractClaims(token).getSubject();
            log.info("SUBJECT {}", subject);
            return subject;
        } catch (RuntimeException e) {
            log.error("ERROR IN extractUserNameFromToken: {}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String username, UserDetails userDetails, String token) {
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

}
