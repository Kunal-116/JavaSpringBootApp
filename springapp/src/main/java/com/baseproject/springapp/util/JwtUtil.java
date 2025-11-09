
/* Generate and validate JWT tokens. */

package com.baseproject.springapp.util;

import java.security.Key;
import java.util.Date;

import org.hibernate.annotations.Comments;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
private final Key key;
private final long expirationMs;

public JwtUtil(
    @Value("${jwt.secret:MyJwtSecretKey12345678901234567890}") String secret,
    @Value("${jwt.expiration-ms:3600000}") long expirationMs
){
   if (secret.length() < 32) {
    this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // auto-generate secure key
} else {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
}
    this.expirationMs=expirationMs;
}

public String generateToken(String u_name)
{     

    Date now=new Date();
    Date expiry=new Date(now.getTime()+expirationMs);

    return Jwts.builder().setSubject(u_name).setIssuedAt(now).setExpiration(expiry).signWith(key,SignatureAlgorithm.HS256).compact();
}

public String getUserName(String token)
{
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
}

private boolean isTokenExpire(String token)
{
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration().before(new Date());
}

public boolean validateToken(String token, UserDetails userDetails) {
    final String username = getUserName(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpire(token));
}
}
