package com.baseproject.springapp.service;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.baseproject.springapp.model.AppUsers;
import com.baseproject.springapp.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

   @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        // 🔑 FIX: Change userRepository to repo (the name of the injected variable)
        AppUsers user = repo.findByumobile(mobile) 
             .orElseThrow(() -> new UsernameNotFoundException("User not found with mobile: " + mobile));

        // 🔑 CRITICAL FIX: Handle the null role defensively
        String userRoles = user.getRole();
        if (userRoles == null || userRoles.trim().isEmpty()) {
            // Assign a default role if none is found, or throw an error if a role is mandatory
            userRoles = "ROLE_USER"; // Default to a safe role if null
        }

        // Now safely split the role string
        return new User(
                user.getUmobile(),
                user.getPassword(),
                Arrays.stream(userRoles.split(","))
                        .map(String::trim) // Trim spaces just in case
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );
    }
}
