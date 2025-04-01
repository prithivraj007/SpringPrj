package com.pricingmicroservice;

import jakarta.persistence.Cacheable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Cacheable
public class SpringPrjApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringPrjApplication.class, args);
	}

}
