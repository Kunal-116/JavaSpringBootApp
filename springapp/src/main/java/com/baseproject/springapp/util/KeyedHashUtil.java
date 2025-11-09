
/* Hashes passwords using your secret key + BCrypt (extra security layer). */

package com.baseproject.springapp.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class KeyedHashUtil {
    private static final String SECRET_KEY = "ExPence78874@";
    private static final BCryptPasswordEncoder encoder= new BCryptPasswordEncoder();

    public static String hashPassword(String u_password)
    {
        String combined=SECRET_KEY+u_password;
        return encoder.encode(combined);
    }

    public static Boolean verifyPassword(String rowPass, String encodedPass)
    {
        String combined= SECRET_KEY+rowPass;
        return encoder.matches(combined, encodedPass);
    }
}
