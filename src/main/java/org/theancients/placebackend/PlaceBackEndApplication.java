package org.theancients.placebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/*
@EnableScheduling
@SpringBootApplication(exclude= {UserDetailsServiceAutoConfiguration.class}) // exclude is needed to disable default password
public class PlaceBackEndApplication {
	public static void main(String[] args)
		SpringApplication.run(PlaceBackEndApplication.class, args);
	}
} */

@EnableScheduling
@SpringBootApplication(exclude={UserDetailsServiceAutoConfiguration.class})
public class PlaceBackEndApplication extends SpringBootServletInitializer {
	@Override
	protected final SpringApplicationBuilder
	configure(final SpringApplicationBuilder application) {
		return application.sources(PlaceBackEndApplication.class);
	}
	public static void main(final String[] args) throws Exception {
		SpringApplication.run(PlaceBackEndApplication.class, args);
	}
}
