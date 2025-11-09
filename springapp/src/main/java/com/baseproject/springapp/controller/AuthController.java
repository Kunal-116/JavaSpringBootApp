package com.baseproject.springapp.controller;

import com.baseproject.springapp.model.AppUsers;
import com.baseproject.springapp.dto.AuthResponse;
import com.baseproject.springapp.dto.LoginRequest;
import com.baseproject.springapp.dto.RegistrationRequest;
import com.baseproject.springapp.util.JwtUtil;
import com.baseproject.springapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
 private final JwtUtil jwtUtil;  
    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
      this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        try {
            AppUsers created = userService.registerNewUser(request);
            return ResponseEntity.ok("User registered successfully with id: " + created.getId());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Registration failed");
        }
    }

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    try {
        userService.authenticateUser(request); // if wrong → throws exception

        // ✅ Generate JWT token
        String token = jwtUtil.generateToken(request.getMobile());

        return ResponseEntity.ok(new AuthResponse(token)); // return token instead of text
    } 
    catch (IllegalArgumentException ex) {  
        return ResponseEntity.status(401).body(ex.getMessage());
    }
    catch (Exception ex) {
        return ResponseEntity.status(500).body("Login failed: " + ex.getMessage());
    }
}
}
