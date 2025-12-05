// /* DTO for sending JWT token in login response. */

// package com.baseproject.springapp.dto;

// public record AuthResponse(String token) {
// } 

package com.baseproject.springapp.dto;

public class AuthResponse {
    
    private String token;

    // ✅ REQUIRED Constructor that accepts the token String
    public AuthResponse(String token) {
        this.token = token;
    }

    // Getters and Setters (Spring needs these for JSON serialization)

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}