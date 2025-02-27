package com.Mayur.HMC1.Bookings;

import java.time.LocalDate;

import com.Mayur.HMC1.Lab;
import com.Mayur.HMC1.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;

@Entity
@Table(name = "bookings1")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "lab_id", nullable = false)
    private Lab lab;
    
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

//    @Column(nullable = false)
    private String selectedServices;  // Stores selected services as a comma-separated string
    
    @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'Pending'")
    private String status = "Pending";  // Default value is "Pending"

    // Constructors
    public Booking() {}

    public Booking(User user, Lab lab, LocalDate appointmentDate, String selectedServices, String status) {
        this.user = user;
        this.lab = lab;
        this.appointmentDate = appointmentDate;
        this.selectedServices = selectedServices;
        this.status = status;
    }

    // Getters and Setters
    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Lab getLab() {
        return lab;
    }

    public void setLab(Lab lab) {
        this.lab = lab;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSelectedServices() {
        return selectedServices;
    }

    public void setSelectedServices(String selectedServices) {
        this.selectedServices = selectedServices;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
