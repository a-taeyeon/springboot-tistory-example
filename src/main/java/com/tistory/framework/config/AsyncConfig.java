package com.tistory.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class AsyncConfig {

    // 병렬처리
    @Bean(name = "fileUploadExecutor")
    public ExecutorService fileUploadExecutor() {
        return Executors.newFixedThreadPool(10); // 고정된 스레드 풀 크기
    }
}
