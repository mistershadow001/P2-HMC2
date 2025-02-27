package com.Mayur.HMC1.Bookings;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Mayur.HMC1.Lab;
import com.Mayur.HMC1.User;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Get all bookings for a specific user
    List<Booking> findByUser(User user);

    // Get all bookings for a specific lab
    List<Booking> findByLab(Lab lab);

    // Get all bookings for a specific user in a specific lab
    List<Booking> findByUserAndLab(User user, Lab lab);
}
