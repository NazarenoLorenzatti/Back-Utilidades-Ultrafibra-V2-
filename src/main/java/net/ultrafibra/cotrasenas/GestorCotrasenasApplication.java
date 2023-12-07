package net.ultrafibra.cotrasenas;

import net.ultrafibra.cotrasenas.config.JksProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(JksProperties.class)
public class GestorCotrasenasApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestorCotrasenasApplication.class, args);
	}

}
