package com.Mayur.HMC1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LabService {

    @Autowired
    private LabRepository labRepository;

    @Autowired
    private JWTservice jwtService;

    @Autowired
    private AuthenticationManager authManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    // ✅ Create a new Lab
    public Lab createLab(Lab lab) {
        if (labRepository.findByLabName(lab.getLabName()).isPresent()) {
            throw new IllegalArgumentException("Lab with this name already exists!");
        }

        lab.setSubscriptionStartDate(LocalDate.now());
        lab.setSubscriptionEndDate(lab.getSubscriptionStartDate().plusMonths(1));
        lab.setPassword(encoder.encode(lab.getPassword()));

        Lab savedLab = labRepository.save(lab);

        String token = generateToken(savedLab);
        System.out.println("Generated Token: " + token);

        return savedLab;
    }

    // ✅ Generate JWT Token
    private String generateToken(Lab lab) {
        long expirationSeconds = 3600;
        return jwtService.generateToken(lab.getLabName(), "LAB", expirationSeconds);
    }

    // ✅ Verify Lab credentials & return JWT Token
    public String verify(Lab lab) {
        Optional<Lab> existingLabOpt = labRepository.findByLabName(lab.getLabName());

        Lab existingLab = existingLabOpt.orElseThrow(() -> new IllegalArgumentException("Lab not found!"));

        if (!encoder.matches(lab.getPassword(), existingLab.getPassword())) {
            return "Failure: Incorrect password!";
        }

        String token = generateToken(existingLab);
        return token;
    }

    // ✅ Authenticate Lab
    public String authenticateLab(Lab lab) {
        Optional<Lab> existingLabOpt = labRepository.findByLabName(lab.getLabName());
        Lab existingLab = existingLabOpt.orElseThrow(() -> new IllegalArgumentException("Lab not found!"));

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(existingLab.getLabName(), lab.getPassword())
        );

        if (authentication.isAuthenticated()) {
            return generateToken(existingLab);
        }
        return "Failure";
    }

    // ✅ Get all Labs
    public List<Lab> getAllLabs() {
        return labRepository.findAll();
    }

    // ✅ Get a Lab by ID
    public Optional<Lab> getLabById(Long id) {
        return labRepository.findById(id);
    }

    // ✅ Get Lab by Email
    public Lab getLabByEmail(String email) {
        return labRepository.findByEmail(email).orElse(null);
    }

    // ✅ Extract email from JWT Token
    public String extractEmailFromToken(String token) {
        return jwtService.extractUsername(token);
    }

    // ✅ Update Lab details by Email
    public Lab updateLabByEmail(String email, Lab updatedLab) {
        return labRepository.findByEmail(email).map(existingLab -> {
            if (updatedLab.getLabName() != null)
                existingLab.setLabName(updatedLab.getLabName());
            if (updatedLab.getServices() != null)
                existingLab.setServices(updatedLab.getServices());
            if (updatedLab.getPhone() != null)
                existingLab.setPhone(updatedLab.getPhone());

            return labRepository.save(existingLab);
        }).orElse(null);
    }

    // ✅ Get Lab by Lab Name
    public Lab getLabByLabName(String labName) {
        return labRepository.findByLabName(labName).orElse(null);
    }

    // ✅ Delete expired labs (Runs every midnight)
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredLabs() {
        LocalDate today = LocalDate.now();
        List<Lab> expiredLabs = labRepository.findBySubscriptionEndDateBefore(today);
        labRepository.deleteAll(expiredLabs);
        System.out.println("Deleted expired labs: " + expiredLabs.size());
    }
    
    public List<Lab> getLabByAddress(String address) {
    	return labRepository.findByAddress(address);
    	
    }
}
