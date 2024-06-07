package com.tistory.project_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.tistory.framework", "com.tistory.project_api"})
public class ProjectApiApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplicationBuilder(ProjectApiApplication.class).properties(
				"spring.config.location="
				+"classpath:/application.yml"
				+", classpath:/config/database.yml")
				.build();
		app.run(args);
	}

}
