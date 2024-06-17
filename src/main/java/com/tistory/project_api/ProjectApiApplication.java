package com.tistory.project_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.tistory.framework", "com.tistory.project_api"})
public class ProjectApiApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplicationBuilder(ProjectApiApplication.class).properties(
				"spring.config.location="
				+"classpath:/application.yml")
				.build();
		app.addListeners(new ApplicationPidFileWriter()); // pid 를 작성하는 역할을 하는 클래스 선언
		app.run(args);
	}

}
