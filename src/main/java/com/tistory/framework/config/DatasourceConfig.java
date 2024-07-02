package com.tistory.framework.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Getter
@Setter
@Configuration
@Profile("prod")
@Slf4j
public class DatasourceConfig {
    @Autowired
    private Dotenv dotenv;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(dotenv.get("PROD_MYSQL_PROJECT_URL"));
        dataSource.setUsername(dotenv.get("PROD_MYSQL_USERNAME"));
        dataSource.setPassword(dotenv.get("PROD_MYSQL_PASSWORD"));
        return dataSource;
    }
}
