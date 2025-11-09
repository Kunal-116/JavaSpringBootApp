package com.baseproject.springapp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.baseproject.springapp.dto.LoginRequest;
import com.baseproject.springapp.dto.RegistrationRequest;
import com.baseproject.springapp.model.AppUsers;
import com.baseproject.springapp.repository.UserRepository;
import com.baseproject.springapp.util.KeyedHashUtil;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String pepper;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       @Value("${app.security.pepper}") String pepper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.pepper = pepper;
    }

    @Transactional
    public AppUsers registerNewUser(RegistrationRequest request) {
        // basic checks
        if (userRepository.existsByumobile(request.getUsermobile())) {
            throw new IllegalArgumentException("Username already taken");
        }
        // if (userRepository.existsByUname(request.getEmail())) {
        //     throw new IllegalArgumentException("Email already in use");
        // }

        // Combine password + pepper
        String passwordPlusPepper = request.getPassword() + pepper;

        // Hash using BCrypt
        String hashed = passwordEncoder.encode(passwordPlusPepper);

        // Create your AppUsers entity (not Spring's User)
        AppUsers user = new AppUsers();
        user.setUname(request.getUsername());
       user.setUmobile(request.getUsermobile());
        user.setEmail(request.getEmail());
        user.setPassword(hashed);

        return userRepository.save(user);
    }

    // 🔐 LOGIN FUNCTION (this is where your line goes)
  public boolean authenticateUser(LoginRequest request) {

    AppUsers user = userRepository.findByumobile(request.getMobile())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    String rawPasswordPlusPepper = request.getPassword() + pepper;

    if (!passwordEncoder.matches(rawPasswordPlusPepper, user.getPassword())) {
        throw new IllegalArgumentException("Invalid password");
    }

    return true;
}

    //   public boolean validateUserCredentials(String mobile, String password) {
    //     return userRepository.findByUmobile(mobile)
    //         .map(user -> KeyedHashUtil.verifyPassword(password, user.getPassword()))
    //         .orElse(false);
    // }
}
