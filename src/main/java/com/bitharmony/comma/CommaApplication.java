package com.bitharmony.comma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.bitharmony.comma.global.config.NcpConfig;

@SpringBootApplication
@EnableConfigurationProperties({NcpConfig.class})
public class CommaApplication {
	public static void main(String[] args) {
		SpringApplication.run(CommaApplication.class, args);
	}
}