package org.example.bodycheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BodycheckApplication {

	public static void main(String[] args) {
		SpringApplication.run(BodycheckApplication.class, args);
	}

}
