package com.post_show_blues.vine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class VineApplication {

	public static void main(String[] args) {
		SpringApplication.run(VineApplication.class, args);
	}

}
