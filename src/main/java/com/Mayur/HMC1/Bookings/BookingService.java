package com.Mayur.HMC1.Bookings;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Mayur.HMC1.Lab;
import com.Mayur.HMC1.LabRepository;
import com.Mayur.HMC1.User;
import com.Mayur.HMC1.UserRepository;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabRepository labRepository;

    public Booking createBooking(Long id, Long labid, LocalDate appointmentDate , String selectedServices , String status ) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Lab lab = labRepository.findById(labid)
                .orElseThrow(() -> new RuntimeException("Lab not found"));

        System.out.println("Received appointment date: " + appointmentDate); // Debug log

        if (appointmentDate == null) {
            throw new IllegalArgumentException("Appointment date cannot be null");
        }

        Booking booking = new Booking(user, lab, appointmentDate, selectedServices , status );
        return bookingRepository.save(booking);
    }


    public List<BookingDTO> getUserBookings(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return bookingRepository.findByUser(user)
                .stream()
                .map(booking -> new BookingDTO(booking.getLab().getId(), booking.getAppointmentDate()))
                .collect(Collectors.toList());
    }


    // ✅ Corrected Method
    public List<LabBookingDTO> getLabBookings(Long labId) {
        return labRepository.findById(labId)
            .map(lab -> bookingRepository.findByLab(lab)
                .stream()
                .map(booking -> new LabBookingDTO(
                    booking.getUser().getId(),  
                    booking.getUser().getUsername(),
                    booking.getUser().getPhone(),  // Fetch user's phone
                    booking.getUser().getAddress(), // Fetch user's address
                    booking.getAppointmentDate(),  
                    booking.getSelectedServices(),
                    booking.getStatus()
                ))
                .collect(Collectors.toList()))
            .orElseThrow(() -> new RuntimeException("Lab not found"));
    }





}
