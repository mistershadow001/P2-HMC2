package com.Mayur.HMC1;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class LabDetails implements UserDetails {
    
    private final Lab lab;

    public LabDetails(Lab lab) {
        this.lab = lab;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(lab.getRole().name())); 
        // Directly convert enum to a string authority (e.g., "LAB")
    }

    @Override
    public String getPassword() {
        return lab.getPassword();  // Ensure this is hashed in the database
    }

    @Override
    public String getUsername() {
        return lab.getLabName();  // Ensure this matches the login request
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Change if you implement expiration logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Change if you implement lockout logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Change if you implement credential expiration
    }

    @Override
    public boolean isEnabled() {
        return true;  // Change if you handle disabled accounts
    }
}
