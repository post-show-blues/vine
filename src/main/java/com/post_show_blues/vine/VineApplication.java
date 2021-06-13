package com.post_show_blues.vine;

import com.post_show_blues.vine.service.AuthService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class VineApplication {

	public static void main(String[] args) {
		SpringApplication.run(VineApplication.class, args);
	}

}
