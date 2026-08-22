package com.example.contactmanager.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private final SecretKey jwtSecret;
    private final Long jwtExpiration;

    public SecretKey getJwtSecret() {
        return jwtSecret;
    }

    public Long getJwtExpiration() {
        return jwtExpiration;
    }

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") Long expiration) {
        this.jwtSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.jwtExpiration = expiration;

//        System.out.println("Secret inject: " + secret);
//        System.out.println("Secret built: " + jwtSecret);
//        System.out.println("Secret in bytes: " + jwtSecret.getEncoded().length);

    }

    public String generateToken(String username) {

        logger.info("Generating JWT token for username: {}", username);

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(jwtSecret)
                .compact();
    }

    private Claims extractAllClaims(String token) {

        logger.info("Extracting all claims from JWT token");

        return Jwts.parser()
                .verifyWith(jwtSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {

        logger.info("Extracting username from JWT token");

        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {

        logger.info("Extracting expiration date from JWT token");

        return extractAllClaims(token).getExpiration();
    }

    public boolean validateToken(String token, String username) {

        logger.info("Validating JWT token for username: {}", username);

        try {

            final String extractedUsername = extractUsername(token);

            if (!extractedUsername.equals(username)) {
                logger.warn("JWT token validation failed: username does not match");
                return false;
            }

            if(isTokenExpired(token)) {
                logger.warn("JWT token validation failed: token is expired");
                return false;
            }

            return true;

        } catch (ExpiredJwtException e) {
            logger.warn("JWT token validation failed: token is expired");
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

}
