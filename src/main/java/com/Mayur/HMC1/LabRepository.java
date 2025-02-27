package com.Mayur.HMC1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LabRepository extends JpaRepository<Lab, Long> {

    // Find Labs by subscription start date (for deletion after 1 month)
   
    
	Optional<Lab> findByLabName(String labName); // Use Optional for consistency

	Optional<Lab> findByEmail(String email);
	
	List<Lab> findByAddress(String address);

	List<Lab> findBySubscriptionEndDateBefore(LocalDate date);

	

}
