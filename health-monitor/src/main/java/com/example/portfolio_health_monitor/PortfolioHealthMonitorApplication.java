package com.example.portfolio_health_monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@EnableAdminServer
@SpringBootApplication
public class PortfolioHealthMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortfolioHealthMonitorApplication.class, args);
	}

}
