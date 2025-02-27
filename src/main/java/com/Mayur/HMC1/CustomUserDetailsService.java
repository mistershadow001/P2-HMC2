package com.Mayur.HMC1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabRepository labRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("🔍 Searching for username: " + username);

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            System.out.println("✅ Found User: " + userOptional.get().getUsername());
            return new UserCustomUserDetails(userOptional.get());
        }

        Optional<Lab> labOptional = labRepository.findByLabName(username);
        if (labOptional.isPresent()) {
            System.out.println("✅ Found Lab: " + labOptional.get().getLabName());
            return new LabDetails(labOptional.get());
        }

        System.out.println("❌ No user or lab found for: " + username);
        throw new UsernameNotFoundException("User or Lab not found: " + username);
    }

}
