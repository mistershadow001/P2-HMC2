package com.Mayur.HMC1;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTservice {

    @Value("${jwt.secret}") 
    private String secretKey;

    // Get Secret Key
    SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Generate JWT Token with Roles
    public String generateToken(String username, String role, long expirationSeconds) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role != null ? role : "USER"); // Store role in token

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(expirationSeconds)))
                .signWith(getKey()) 
                .compact();
    }

    // Extract Username from Token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract Role from Token
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // Extract Specific Claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Validate JWT Token
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String extractedUsername = extractUsername(token);
            System.out.println("🔐 Extracted Username from Token: " + extractedUsername);
            System.out.println("🔍 Expected Username: " + userDetails.getUsername());

            boolean isValid = extractedUsername.equals(userDetails.getUsername()) && !isTokenExpired(token);
            System.out.println("✅ Token Valid: " + isValid);
            
            return isValid;
        } catch (Exception e) {
            System.out.println("❌ Token Validation Failed: " + e.getMessage());
            return false;
        }
    }


    // Check if Token is Expired
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // Extract All Claims (JJWT 0.12.6 Compatible)
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey()) // Uses `verifyWith(secretKey)`
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
