package com.example.spring_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringApiApplication.class, args);
	}
	@GetMapping("/")
	public String introduction() {
		return """
                Welcome to the Bank Transaction Scheduling API!<br><br>
                This API allows you to:<br>
                - Create, update, delete, and retrieve bank transactions.<br>
                - Automatically calculate transfer fees based on scheduling rules.<br><br>
                Explore the endpoints using the Swagger UI: <a href="/swagger-ui.html">Swagger Documentation</a>
                """;
	}
}