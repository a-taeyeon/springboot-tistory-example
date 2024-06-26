package com.tistory.framework.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod") // 이 클래스는 prod 프로파일에서만 활성화됩니다.
public class DotenvConfig {
    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure()
                .directory("/keys") // resources 디렉토리 내의 경로 (resources 디렉토리는 classpath에 포함됨)
                .filename("prod.env") // 사용할 환경 변수 파일 명시
                .ignoreIfMissing() // 파일이 없어도 무시
                .load();
    }
    @Bean
    public String PROD_MYSQL_PROJECT_URL(Dotenv dotenv) {
        return dotenv.get("PROD_MYSQL_PROJECT_URL");
    }

    @Bean
    public String PROD_MYSQL_USERNAME(Dotenv dotenv) {
        return dotenv.get("PROD_MYSQL_USERNAME");
    }

    @Bean
    public String PROD_MYSQL_PASSWORD(Dotenv dotenv) {
        return dotenv.get("PROD_MYSQL_PASSWORD");
    }


}
