package dev.tylerpac.barpal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import dev.tylerpac.barpal.ollama.OllamaProperties;

@SpringBootApplication
@EnableConfigurationProperties({OllamaProperties.class})
public class BarpalApplication {

	public static void main(String[] args) {
		SpringApplication.run(BarpalApplication.class, args);
	}

}
