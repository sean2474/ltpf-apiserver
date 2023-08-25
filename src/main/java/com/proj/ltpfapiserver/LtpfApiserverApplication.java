package com.proj.ltpfapiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class LtpfApiserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(LtpfApiserverApplication.class, args);
	}
}
