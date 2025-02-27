package com.Mayur.HMC1.Bookings;

import java.time.LocalDate;

public class BookingDTO {
    private Long labId;
    private LocalDate appointmentDate;

    public BookingDTO(Long labId, LocalDate appointmentDate) {
        this.labId = labId;
        this.appointmentDate = appointmentDate;
    }

    public Long getLabId() {
        return labId;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }
}
