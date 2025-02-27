package com.Mayur.HMC1;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/labs")
@CrossOrigin(origins = "http://localhost:5173")
public class LabController {

    private final LabService labs;
    private final CustomUserDetailsService labdetails;
    private final LabRepository repo;
    private final JWTservice jwtservice;

    public LabController(LabService labs, CustomUserDetailsService labdetails, LabRepository repo, JWTservice jwtservice) {
        this.labs = labs;
        this.labdetails = labdetails;
        this.repo = repo;
        this.jwtservice = jwtservice;
    }

    // ✅ Register a New Lab
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerLab(@RequestBody Lab lab) {
        labs.createLab(lab);
        return ResponseEntity.ok(Map.of("message", "Registered successfully"));
    }

    // ✅ Login and Get JWT Token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Lab lab) {
        String token = labs.verify(lab);
        return (token != null)
                ? ResponseEntity.ok(Map.of("token", token))
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
    }

    // ✅ Get Current Lab Name
    @GetMapping("/current-lab")
    @PreAuthorize("hasAuthority('LAB')")// Ensures only logged-in labs can access
    public ResponseEntity<Map<String, String>> getCurrentLabName(Authentication authentication) {
        return ResponseEntity.ok(Map.of("labName", authentication.getName()));
    }

    // ✅ Get Lab Profile Details
    @GetMapping("/lab-profile")
    @PreAuthorize("hasAuthority('LAB')")
    public ResponseEntity<?> getLabProfile(Authentication authentication) {
        Lab lab = labs.getLabByLabName(authentication.getName());
        return (lab != null) ? ResponseEntity.ok(lab) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Lab not found"));
    }

    // ✅ Update Lab Profile
    @PutMapping("/update-profile")
    @PreAuthorize("hasAuthority('LAB')")
    public ResponseEntity<?> updateLabProfile(Authentication authentication, @RequestBody Lab updatedLab) {
        Lab lab = labs.updateLabByEmail(authentication.getName(), updatedLab);
        return (lab != null) ? ResponseEntity.ok(lab) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Lab not found"));
    }

    // ✅ Get List of All Labs (Only for Admins)
    @GetMapping("/show")
    public ResponseEntity<List<Lab>> showAllLabs() {
        return ResponseEntity.ok(labs.getAllLabs());
    }
    @GetMapping("/search-labs")
    public ResponseEntity<List<Lab>> searchLabs(@RequestParam String address) {
        List<Lab> labS = labs.getLabByAddress(address);
        return ResponseEntity.ok(labS);
    }
    
    @GetMapping("lab/{labId}")
    @PreAuthorize("hasAuthority('USER')")
    public Optional<Lab> getLabById(@PathVariable Long labId) {
        Optional<Lab> lab = labs.getLabById(labId);
        return lab;
    }             
}
