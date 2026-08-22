package com.example.contactmanager.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private static final String testSecretKey = "tOa85eJUdxHJXnbCim43rvF9SF9chdH65ns6xSCp2/A=";
    private static final Long testExpirationTime = 24 * 1000 * 60 * 60L; // 1 day in milliseconds

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(testSecretKey, testExpirationTime);
    }

    //================= GENERATE TOKEN =====================

    @Test
    void generateToken_ShouldReturnValidToken() {
        // Arrange
        String username = "test-user";

        // Act
        String token = jwtService.generateToken(username);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals(username, jwtService.extractUsername(token));
        assertEquals(3, token.split("\\.").length); // JWT should have 3 parts

    }

    //================= EXTRACT USERNAME =====================

    @Test
    void extractUsername_ShouldReturnCorrectUsername_WhenTokenIsValid() {
        String username = "test-user";
        String token = jwtService.generateToken(username);
        assertNotNull(token);

        String extractedUsername = jwtService.extractUsername(token);

        assertEquals(username, extractedUsername);

    }

    //================= EXTRACT EXPIRATION =====================

    @Test
    void extractExpiration_ShouldReturnCorrectExpiration_WhenTokenIsValid() {
        String username = "test-user";
        Long beforeGeneration = System.currentTimeMillis();

        String token = jwtService.generateToken(username);
        Date extractedExpiration = jwtService.extractExpiration(token);

        Long expectedExpiration = beforeGeneration + testExpirationTime;
        Long actualExpiration = extractedExpiration.getTime();

        assertEquals(expectedExpiration, actualExpiration, 1000); // Allow 1 second difference

    }

    //================= VALIDATE TOKEN =====================

    @Test
    void validateToken_ShouldReturnTrue_WhenTokenIsValid() {

        String username = "test-user";
        String token = jwtService.generateToken(username);

        assertTrue(jwtService.validateToken(token, username));

    }

    @Test
    void validateToken_ShouldReturnFalse_WhenUsernameDoesNotMatch() {
        String username = "test-user";
        String token = jwtService.generateToken(username);

        assertFalse(jwtService.validateToken(token, "wrong-username"));

    }

    @Test
    void validateToken_ShouldReturnFalse_WhenTokenIsExpired() throws InterruptedException {

        JwtService shortLivedJwtService = new JwtService(testSecretKey, 1000L); // 1 second expiration
        String username = "test-user";
        String token = shortLivedJwtService.generateToken(username);

        Thread.sleep(1500L); // Sleep for 1.5 seconds

        assertFalse(shortLivedJwtService.validateToken(token, username));
    }
}