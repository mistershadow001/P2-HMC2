package com.Mayur.HMC1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService; // Handles both users & labs

    @Autowired
    private jwtfilter jwtFilter; // JWT authentication filter

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for API calls
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
            .authorizeHttpRequests(auth -> auth
            	    .requestMatchers("/labs/register", "/labs/login", "/labs/show", "/labs/search-labs", "/users/register", "/users/login").permitAll()
            	    .requestMatchers("/admin/**").hasAuthority("ADMIN") // Admin-only access
            	    .requestMatchers("/labs/lab/**").hasAuthority("USER") // Allow USERS to access /labs/lab/**
            	    .requestMatchers("/labs/**").hasAuthority("LAB") // LAB role for other /labs endpoints
            	    .requestMatchers("/users/**").hasAuthority("USER")
            	    .requestMatchers("/bookings/update-status/**").hasRole("LAB")
            	    .requestMatchers("/bookings/labs/**").hasAuthority("LAB")
            	    .requestMatchers("/bookings/**").hasAuthority("USER")
            	    .anyRequest().authenticated()
            	)

            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authProvider()) // Use DAO authentication
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Apply JWT filter
            .exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler())); // Custom error handling

        return http.build();
    }

    @Bean
    public AuthenticationProvider authProvider() { 
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder()); // Use the bean
        provider.setUserDetailsService(customUserDetailsService); // ✅ Use merged UserDetailsService
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ✅ Enable CORS for React Frontend
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Allow React frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // ✅ Custom Access Denied Handler (Debugging)
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (HttpServletRequest request, HttpServletResponse response, org.springframework.security.access.AccessDeniedException ex) -> {
            System.out.println("❌ Access Denied for request: " + request.getRequestURI());
            System.out.println("Reason: " + ex.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "403 Forbidden - Access Denied");
        };
    }
}
