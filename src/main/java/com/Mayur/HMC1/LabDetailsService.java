//package com.Mayur.HMC1;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service("LabDetailsService") 
//public class LabDetailsService implements UserDetailsService {
//
//    @Autowired
//    private LabRepository labRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Lab lab = labRepository.findByLabName(username)
//                .orElseThrow(() -> new UsernameNotFoundException("Lab not found: " + username));
//
//        return new LabDetails(lab); // Return LabDetails, which implements UserDetails
//    }
//
//}
