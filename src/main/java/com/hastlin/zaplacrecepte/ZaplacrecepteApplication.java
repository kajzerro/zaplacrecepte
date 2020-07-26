package com.hastlin.zaplacrecepte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ZaplacrecepteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZaplacrecepteApplication.class, args);
	}
	//TODO: Filtracja adresów IP z których mogą przychodzić update
	//TODO: Przetestować wszystkie scenariusze płacenia/niepłacenia/odrzucenia płatności

}
