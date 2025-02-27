package com.Mayur.HMC1;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTservice jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTservice jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String registerUser(String username, String email, String password , String phone, String address) {
        // Hash the password
        String hashedPassword = passwordEncoder.encode(password);

        // Create a new user with a default role
        User newUser = new User(username, email, hashedPassword, "USER", phone, address);

        // Save the user to the database
        userRepository.save(newUser);

        // Generate a JWT token for the user
        return jwtService.generateToken(newUser.getUsername(), newUser.getRole(), 86400);
    }
    
    

    public String verify(User user) {
        Optional<User> existingUserOpt = userRepository.findByUsername(user.getUsername());

        // Check if user exists
        User existingUser = existingUserOpt.orElseThrow(() -> new IllegalArgumentException("User not found!"));

        // Check if password matches
        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return "Failure: Incorrect password!";
        }

        // Generate JWT Token
       String token = jwtService.generateToken(existingUser.getUsername(), existingUser.getRole(), 86400);
        return token;
    }

	public Optional<User> getUserByEmail(String name) {
		return userRepository.findByEmail(name);
		
	}

	public Optional<User> getUserByName(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		return user;
	}

	


}
