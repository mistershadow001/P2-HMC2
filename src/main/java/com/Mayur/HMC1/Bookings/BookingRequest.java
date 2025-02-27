package com.Mayur.HMC1.Bookings;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

public class BookingRequest {
    private Long userId;
    private Long labId;

    @JsonFormat(pattern = "yyyy-MM-dd") // Ensures correct JSON parsing
    private LocalDate appointmentDate;

    private String selectedServices; // Services selected by the user
    private String status = "Pending"; // Default status

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLabId() {
        return labId;
    }

    public void setLabId(Long labId) {
        this.labId = labId;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
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
