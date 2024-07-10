package com.zion;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
@SpringBootApplication
@OpenAPIDefinition
public class CookBookProApplication {

	public static void main(String[] args) {
		SpringApplication.run(CookBookProApplication.class, args);
	}

}
