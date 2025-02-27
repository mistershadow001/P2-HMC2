package com.Mayur.HMC1;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.crypto.SecretKey;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JWTservice jwtService;
    private final UserRepository userRepository; // ✅ Added userRepository

    public UserController(UserService userService, JWTservice jwtService, UserRepository userRepository) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.userRepository = userRepository; // ✅ Initialized userRepository
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String email = body.getOrDefault("email", ""); // Prevents NullPointerException
        String password = body.getOrDefault("password", "");
        String phone = body.getOrDefault("phone", "");
        String address = body.getOrDefault("address", "");

        if (username == null || email.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid input: All fields are required.");
        }

        System.out.println("Received User: " + username);
        
        // Register user and get token
        String token = userService.registerUser(username, email, password, phone, address);
        
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        String token = userService.verify(user);
        return (token != null)
                ? ResponseEntity.ok(Map.of("token", token))
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated!");
        }

        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }
    }
    
    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getUserIdFromToken(@RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", ""); // Remove "Bearer " prefix

            // Get key from your JWT service
            SecretKey key = jwtService.getKey(); 

            // Correct JWT parsing method for JJWT 0.12.6+
            JwtParser jwtParser = Jwts.parser().verifyWith(key).build();
            Claims claims = jwtParser.parseSignedClaims(token).getPayload();

            String username = claims.getSubject(); // Extract username from token

            Optional<User> user = userRepository.findByUsername(username);
            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
            }

            Map<String, Object> response = new HashMap<>();
            response.put("userId", user.get().getId());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token"));
        }
    }







}
