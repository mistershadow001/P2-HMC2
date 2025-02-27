package com.Mayur.HMC1;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class jwtfilter extends OncePerRequestFilter {
	
    @Autowired
    private JWTservice jwtService;

    @Autowired
    private UserDetailsService userDetailsService; // ✅ Injected directly

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println("🔍 Incoming Request Path: " + path);  // ✅ Debug Log

     // 🚀 Skip JWT validation for login and register endpoints
        if (path.equals("/labs/login") || path.equals("/labs/register") || 
        	    path.equals("/users/login") || path.equals("/users/register") || path.equals("/labs/show")|| path.equals("/labs/search-labs")) {
        	    
        	    System.out.println("✅ Skipping JWT Filter for " + path);
        	    filterChain.doFilter(request, response);
        	    return;
        	}



        // Extract JWT token from Authorization header
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Remove "Bearer " prefix
            username = jwtService.extractUsername(token); // Extract username from token
        }

        System.out.println("🛠 Extracted Token: " + token);
        System.out.println("👤 Extracted Username: " + username);

        // Validate JWT and set authentication
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            System.out.println("🔐 User Found: " + userDetails.getUsername());
            System.out.println("🎭 User Roles: " + userDetails.getAuthorities());

            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("✅ Authentication Set for User: " + username);
            } else {
                System.out.println("❌ Invalid Token for User: " + username);
            }
        }

        // Continue request processing
        filterChain.doFilter(request, response);
    }
}
