

package com.baseproject.springapp.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JwtUtil {
    private final Key key;
    private final long expirationMs;

    public JwtUtil(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.expiration-ms}") long expirationMs
    ){
        // Ensure secret is long enough (32 bytes / 256 bits for HS256)
        if (secret.length() < 32) {
             this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256); 
        } else {
             this.key = Keys.hmacShaKeyFor(secret.getBytes());
        }
        this.expirationMs=expirationMs;
    }

    // New method to extract all claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // New method to extract a specific claim (like mobile/subject)
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    // Retrieves the mobile number (Subject)
    public String getUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    // ✅ NEW: Retrieves the user ID from claims
    public Long getUserId(String token) {
        // Retrieve the 'userId' claim and safely cast it to Long
        Integer userIdInt = extractClaim(token, claims -> claims.get("userId", Integer.class));
        return userIdInt != null ? userIdInt.longValue() : null;
    }


    // Updated to accept user ID
    public String generateToken(String u_mobile, Long user_id) { 
        Map<String, Object> claims = new HashMap<>();
        // ✅ CRITICAL: Add user_id as a custom claim
        claims.put("userId", user_id); 
        
        Date now=new Date();
        Date expiry=new Date(now.getTime()+expirationMs);

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(u_mobile)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    // ... (isTokenExpire and validateToken remain the same, ensuring they use updated extractClaim logic if necessary)

    private boolean isTokenExpire(String token) {
         return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpire(token));
    }

    // public Long getUserId(String token) {
    //     // We look for a claim named 'user_id' (or whatever you named it during generation)
    //     Object userId = extractClaim(token, claims -> claims.get("user_id"));
        
    //     // Ensure the returned value is converted correctly to Long
    //     if (userId instanceof Integer) {
    //         return ((Integer) userId).longValue();
    //     } else if (userId instanceof Long) {
    //         return (Long) userId;
    //     } else {
    //         // Handle error case or return null
    //         return null;
    //     }
    // }
}
