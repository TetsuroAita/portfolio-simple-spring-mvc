package com.example.portfolio_simple_spring_mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PortfolioSimpleSpringMvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortfolioSimpleSpringMvcApplication.class, args);
	}
}
