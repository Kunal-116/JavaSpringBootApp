package com.baseproject.springapp.util;

import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
    
 public Long getLoggedInUserId() {

        org.springframework.security.core.Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        Object details = auth.getDetails();

        if (details instanceof Map) {
            Object userId = ((Map<?, ?>) details).get("userId");
            if (userId instanceof Long) {
                return (Long) userId;
            }
        }

        throw new RuntimeException("UserId not found in session");
    }
}
