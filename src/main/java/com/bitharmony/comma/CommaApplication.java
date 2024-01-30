package com.bitharmony.comma;

import com.bitharmony.comma.global.config.NcpConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({NcpConfig.class})
public class CommaApplication {
	public static void main(String[] args) {
		SpringApplication.run(CommaApplication.class, args);
	}
}