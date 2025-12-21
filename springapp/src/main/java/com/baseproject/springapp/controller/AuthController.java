

package com.baseproject.springapp.controller;
import com.baseproject.springapp.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse; 
import com.baseproject.springapp.model.AppUsers;
import com.baseproject.springapp.dto.RegistrationRequest;
import com.baseproject.springapp.dto.LoginRequest;
import com.baseproject.springapp.dto.AuthResponse; // DTO to hold the token
import com.baseproject.springapp.util.CookieUtil;
import com.baseproject.springapp.util.JwtUtil;
import com.baseproject.springapp.service.UserService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; // IMPORTANT

@RestController // ✅ Use RestController
@RequestMapping("/api/auth") 
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil; // ✅ Re-add JWT dependency

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // Registration (API remains the same)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        try {
            AppUsers created = userService.registerNewUser(request);
            return ResponseEntity.ok("User registered successfully. User ID: " + created.getId());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Registration failed: An unexpected error occurred.");
        }
    }


   @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
    try {
        System.out.println(request);
        // 1. Authenticate user and retrieve the full AppUsers object
        AppUsers user = userService.authenticateAndRetrieveUser(request); 

        // 2. Generate JWT token, passing the required arguments
        String token = jwtUtil.generateToken(user.getUmobile(), user.getId()); 
        
        CookieUtil.create(response, token);
       Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);
        responseBody.put("message", "Login successful");
        
        return ResponseEntity.ok(responseBody);
        
    } catch (UsernameNotFoundException | BadCredentialsException e) {
        // Handle specific authentication failures
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid mobile or password");
    } catch (Exception e) {
        // Catch any other unexpected errors
        System.err.println("Login error: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed due to a server error.");
    }
}

@PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
       
        CookieUtil.clear(response);
        return ResponseEntity.ok("Successfully logged out");
    }
}