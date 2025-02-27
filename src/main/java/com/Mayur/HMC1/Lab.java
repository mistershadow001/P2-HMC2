package com.Mayur.HMC1;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

import com.Mayur.HMC1.Bookings.Booking;



@Entity
@Table(name = "labs1")
public class Lab  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String labName;  // Used as the login username

    private String ownerName;
    private String phone;
    private String email;
    private String address;
    
    @ElementCollection
    @Lob 
    @CollectionTable(name = "services", joinColumns = @JoinColumn(name = "lab_id"))
    @Column(name = "services")
    private List<String> services;

    private LocalDate subscriptionStartDate;
    private LocalDate subscriptionEndDate;

    @Column(nullable = false, unique = true)
    private String paymentId;  // Unique payment identifier

    private String password;  // Hashed before storing

    @Enumerated(EnumType.STRING)  // Stores role as a String in the DB
    @Column(nullable = false)
    private Role role =Role.LAB; 
    
    @OneToMany(mappedBy = "lab", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    public Lab() {
        // Default constructor
    }

    public Lab(String labName, String ownerName, String phone, String email, String address, List<String> services, LocalDate subscriptionStartDate, LocalDate subscriptionEndDate, String paymentId, String password, Role role) {
        this.labName = labName;
        this.ownerName = ownerName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.services = services;
        this.subscriptionStartDate = subscriptionStartDate;
        this.subscriptionEndDate = subscriptionEndDate;
        this.paymentId = paymentId;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public LocalDate getSubscriptionStartDate() {
        return subscriptionStartDate;
    }

    public void setSubscriptionStartDate(LocalDate subscriptionStartDate) {
        this.subscriptionStartDate = subscriptionStartDate;
    }

    public LocalDate getSubscriptionEndDate() {
        return subscriptionEndDate;
    }

    public void setSubscriptionEndDate(LocalDate subscriptionEndDate) {
        this.subscriptionEndDate = subscriptionEndDate;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password; // Will be hashed before saving
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
