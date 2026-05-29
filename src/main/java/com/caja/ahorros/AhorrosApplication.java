package com.caja.ahorros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AhorrosApplication {

	public static void main(String[] args) {
		System.out.println("DB_URL: " + System.getenv("DATABASE_URL"));
		SpringApplication.run(AhorrosApplication.class, args);
	}

}
