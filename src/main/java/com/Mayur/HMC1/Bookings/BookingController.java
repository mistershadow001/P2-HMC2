package com.Mayur.HMC1.Bookings;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/bookings")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private BookingRepository bookingrepo;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest request) {
        Booking newBooking = bookingService.createBooking(
            request.getUserId(),
            request.getLabId(),
            request.getAppointmentDate(),
            request.getSelectedServices(),  // Pass selected services
            request.getStatus()  // Pass status (default is "Pending")
        );
        return ResponseEntity.ok(newBooking);
    }


    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<BookingDTO>> getUserBookings(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getUserBookings(userId));
    }

    // ✅ New Endpoint to Get Bookings for a Specific Lab
    @GetMapping("/labs/{labId}")
    @PreAuthorize("hasAuthority('LAB')")
    public List<LabBookingDTO> getLabBookings(@PathVariable Long labId) {
        return bookingService.getLabBookings(labId);
    }
    
    @PutMapping("/bookings/update-status/{bookingId}")
    @PreAuthorize("hasAuthority('LAB')")
    public ResponseEntity<String> updateBookingStatus(@PathVariable Long bookingId, @RequestBody Map<String, String> request) {
        return bookingrepo.findById(bookingId)
            .map(booking -> {
                booking.setStatus(request.get("status"));
                bookingrepo.save(booking);
                return ResponseEntity.ok("Status updated successfully");
            })
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found"));
    }

}
