package com.brandonorellana.Tienda_Suma;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class TiendaSumaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TiendaSumaApplication.class, args);
		log.info("Corriendo correctamente.....");
	}
}
