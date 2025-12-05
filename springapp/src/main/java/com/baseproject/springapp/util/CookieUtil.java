package com.baseproject.springapp.util;

// 1. Correct import for the Cookie class
import jakarta.servlet.http.Cookie;

// 2. Correct import for HttpServletResponse
import jakarta.servlet.http.HttpServletResponse; 
// You will also need to fix the HttpServletRequest import in the same file
import jakarta.servlet.http.HttpServletRequest;

public class CookieUtil {

    private static final String JWT_COOKIE_NAME = "jwt";

    public static void create(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, token);
        
        // Settings for security and functionality
        cookie.setHttpOnly(true); // Prevents JavaScript access (XSS defense)
        cookie.setSecure(true);   // Only send over HTTPS (HIGHLY Recommended)
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days (Set your token validity time)
        cookie.setPath("/");      // Available to the entire application
        
        response.addCookie(cookie);
    }

    public static void clear(HttpServletResponse response) {
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0); // Immediately expire the cookie
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static String getJwtToken(jakarta.servlet.http.HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (JWT_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}