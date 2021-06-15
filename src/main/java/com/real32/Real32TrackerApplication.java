package com.real32;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.real32.models.Role;
import com.real32.repositories.RoleRepository;

@SpringBootApplication
public class Real32TrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(Real32TrackerApplication.class, args);
	}

	@Bean
	CommandLineRunner init(RoleRepository roleRepository) {

		return args -> {
			Role adminRole;
			try {
				adminRole = roleRepository.findByRole("ADMIN");
				if (adminRole == null) {
					Role newAdminRole = new Role();
					newAdminRole.setRole("ADMIN");
					roleRepository.save(newAdminRole);
					System.out.println("Created ADMIN");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Role userRole = roleRepository.findByRole("USER");
			if (userRole == null) {
				Role newUserRole = new Role();
				newUserRole.setRole("USER");
				roleRepository.save(newUserRole);
			}
		};

	}

}
