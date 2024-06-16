package com.tistory.framework.docs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tistory project-api")
                        .version("1.0")
                        .description("🍉Tistory 작성을 위한 project-api🍉\n\n" +
                                "👽목적: 다양한 기술을 구현해보기\n")
                        .contact(new Contact()
                                .name("태이오이오")
//                                .email("your-email@domain.com"))
                                .url("https://tyan-8.tistory.com/")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer"))
                .components(new io.swagger.v3.oas.models.Components() // 보안 스키마 정의
                        .addSecuritySchemes("Bearer", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}