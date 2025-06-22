package com.github.sysdepen.depen_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackages = "com.github.sysdepen.depen_api")
public class DepenApiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(DepenApiApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(DepenApiApplication.class);
	}
}

//@SpringBootApplication
//public class DepenApiApplication {
//	public static void main(String[] args) {
//		SpringApplication.run(DepenApiApplication.class, args);
//	}
//}
