package org.theancients.placebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude= {UserDetailsServiceAutoConfiguration.class}) // exclude is needed to disable default password
@EnableScheduling
public class PlaceBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaceBackEndApplication.class, args);
	}

}
