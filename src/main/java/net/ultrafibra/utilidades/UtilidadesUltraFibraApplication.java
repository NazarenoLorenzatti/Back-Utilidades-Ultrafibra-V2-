package net.ultrafibra.utilidades;

import net.ultrafibra.utilidades.config.JksProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableConfigurationProperties(JksProperties.class)
public class UtilidadesUltraFibraApplication {

	public static void main(String[] args) {
		SpringApplication.run(UtilidadesUltraFibraApplication.class, args);
	}

}
