// package com.baseproject.springapp.filter;

// import com.baseproject.springapp.service.CustomUserDetailsService;
// import com.baseproject.springapp.util.JwtUtil;
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;
// import java.io.IOException;

// @Component
// public class JwtAuthenticationFilter extends OncePerRequestFilter {

//     @Autowired
//     private JwtUtil jwtUtil;

//     @Autowired
//     private CustomUserDetailsService customUserDetailsService;

//     @Override
//     protected void doFilterInternal(HttpServletRequest request,
//                                     HttpServletResponse response,
//                                     FilterChain filterChain)
//             throws ServletException, IOException {

//         String authHeader = request.getHeader("Authorization");
//         String mobile = null;
//         String token = null;

//         // Extract Bearer token
//         if (authHeader != null && authHeader.startsWith("Bearer ")) {
//             token = authHeader.substring(7);
//             try {
//                 mobile = jwtUtil.getUserName(token); // subject = u_mobile
//             } catch (Exception ex) {
//                 System.out.println("JWT Parse Failed: " + ex.getMessage());
//             }
//         }

//         // Validate & set security context
//         if (mobile != null && SecurityContextHolder.getContext().getAuthentication() == null) {

//             UserDetails userDetails = customUserDetailsService.loadUserByUsername(mobile);

//             if (jwtUtil.validateToken(token, userDetails)) {
//                 UsernamePasswordAuthenticationToken authToken =
//                         new UsernamePasswordAuthenticationToken(
//                                 userDetails, null, userDetails.getAuthorities());

//                 authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                 SecurityContextHolder.getContext().setAuthentication(authToken);
//             }
//         }

//         filterChain.doFilter(request, response);
//     }
// }

package com.baseproject.springapp.filter;
import java.util.Map;
import com.baseproject.springapp.service.CustomUserDetailsService;
import com.baseproject.springapp.util.CookieUtil;
import com.baseproject.springapp.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils; // Import this utility
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.HashMap;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;
        String mobile = null;
        Long userId = null;

        // 1. 🔑 PRIMARY CHECK: Get token from the HTTP-Only cookie
        token = CookieUtil.getJwtToken(request);
        
        // 2. FALLBACK CHECK: If cookie is null, check the Authorization header (optional for API compatibility)
        if (!StringUtils.hasText(token)) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
        }
        
        // 3. Process the token if found
        if (StringUtils.hasText(token)) {
            try {
                // Assuming getUserName extracts the mobile number (Subject)
                mobile = jwtUtil.getUserName(token); 
                userId = jwtUtil.getUserId(token);
            } catch (Exception ex) {
                // Failed to parse or validate the token (e.g., expired)
                System.out.println("JWT Parse/Validation Failed: " + ex.getMessage());
                // We clear the invalid cookie/token to prevent infinite loops/errors
                CookieUtil.clear(response); 
                token = null; // Invalidate the token for the rest of the method
            }
        }


        // 4. Validate & set security context
        // Only proceed if mobile is extracted and no authentication context is set yet
        if (mobile != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(mobile);

            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
             WebAuthenticationDetails webDetails = new WebAuthenticationDetailsSource().buildDetails(request);
      
        Map<String, Object> customDetailsMap = new HashMap<>();
        customDetailsMap.put("userId", userId);
        customDetailsMap.put("webDetails", webDetails);

        authToken.setDetails(customDetailsMap);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}