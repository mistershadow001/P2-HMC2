package com.Mayur.HMC1.Bookings;

import java.time.LocalDate;

public class LabBookingDTO {
    private Long userId;
    private String username;
    private String phone;
    private String address;
    private LocalDate appointmentDate;
    private String selectedServices;
    private String status; // Added field for booking status

    public LabBookingDTO(Long userId, String username, String phone, String address, LocalDate appointmentDate, String selectedServices, String status) {
        this.userId = userId;
        this.username = username;
        this.phone = phone;
        this.address = address;
        this.appointmentDate = appointmentDate;
        this.selectedServices = selectedServices;
        this.status = status;
    }

    // Getters
    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public String getSelectedServices() {
        return selectedServices;
    }

    public String getStatus() {
        return status;
    }

    
}
