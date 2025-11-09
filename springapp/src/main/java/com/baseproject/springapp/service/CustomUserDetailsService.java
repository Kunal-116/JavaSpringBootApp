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
    public UserDetails loadUserByUsername(String UMobile) throws UsernameNotFoundException {
        AppUsers user = repo.findByumobile(UMobile)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with mobile: " + UMobile));

        var authorities = Arrays.stream(user.getRole().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new User(user.getUmobile(), user.getPassword(), authorities);
    }
}
