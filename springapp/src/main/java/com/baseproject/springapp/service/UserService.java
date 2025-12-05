package com.baseproject.springapp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.baseproject.springapp.dto.LoginRequest;
import com.baseproject.springapp.dto.RegistrationRequest;
import com.baseproject.springapp.model.AppUsers;
import com.baseproject.springapp.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String pepper; // The injected secret pepper

    public UserService(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        @Value("${app.security.pepper}") String pepper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.pepper = "ExPence78874@";
    }

    @Transactional
    public AppUsers registerNewUser(RegistrationRequest request) {
        if (userRepository.existsByumobile(request.getUsermobile())) {
            throw new IllegalArgumentException("Mobile number already taken");
        }

        // ✅ REGISTRATION: Combine raw password + pepper before hashing
        String passwordPlusPepper = request.getPassword() + pepper;
        String hashed = passwordEncoder.encode(passwordPlusPepper);

        AppUsers user = new AppUsers();
        user.setUname(request.getUsername());
        user.setUmobile(request.getUsermobile());
        user.setEmail(request.getEmail());
        user.setPassword(hashed); // Store Hash(Password + Pepper)

        return userRepository.save(user);
    }

    // 🔐 LOGIN FUNCTION: Authenticate user and retrieve object for JWT generation
    public AppUsers authenticateAndRetrieveUser(LoginRequest request) {
        
        // 1. Fetch the user by mobile number
        AppUsers user = userRepository.findByumobile(request.getMobile())
            // Throw Spring Security exception if not found
            .orElseThrow(() -> new UsernameNotFoundException("User not found for mobile: " + request.getMobile()));

        // 2. Prepare the incoming password by adding the same pepper
        String pepperedPassword = request.getPassword() + this.pepper;
        
        // 3. Verify: Check if Hash(Input Password + Pepper) matches Stored Hash
        if (!passwordEncoder.matches(pepperedPassword, user.getPassword())) {
            // Throw Spring Security exception on failure
            throw new BadCredentialsException("Invalid password"); 
        }
        
        // 4. Return the full user object on success
        return user;
    }
}