package com.tistory.project_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ProjectApiApplication {

	public static void main(String[] args) {
//		SpringApplication.run(ProjectApiApplication.class, args);
		SpringApplication app = new SpringApplicationBuilder(ProjectApiApplication.class).properties(
				"spring.config.location="
				+"classpath:/application.yml"
				+", classpath:/config/database.yml")
				.build();
		app.run(args);
	}

}
