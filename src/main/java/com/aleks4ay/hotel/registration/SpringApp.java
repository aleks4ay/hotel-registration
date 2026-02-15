package com.aleks4ay.hotel.registration;

import com.aleks4ay.hotel.registration.config.properties.KeycloakClientProperties;
import com.aleks4ay.hotel.registration.config.properties.RoleProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({RoleProperties.class, KeycloakClientProperties.class})
@SpringBootApplication
public class SpringApp {

	public static void main(String[] args) {
		SpringApplication.run(SpringApp.class, args);
	}
}