package com.veckos.VECKOS_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VeckosBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(VeckosBackendApplication.class, args);
	}

}
