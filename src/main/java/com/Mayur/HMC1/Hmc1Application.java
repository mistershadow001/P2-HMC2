package com.Mayur.HMC1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // This enables scheduled tasks
public class Hmc1Application {

	public static void main(String[] args) {
		SpringApplication.run(Hmc1Application.class, args);
	}

}
