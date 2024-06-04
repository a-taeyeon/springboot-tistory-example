package com.tistory.framework.config;

import com.tistory.framework.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Spring 설정 클래스임
@EnableWebSecurity // Spring Security를 활성화
public class SecurityConfig {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    // HTTP 보안 설정을 구성하는 메서드
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> // HTTP 요청에 대한 보안 규칙을 정의
                        authorizeRequests
                                .requestMatchers("/user/**").permitAll() // /public/** 경로에 대한 접근을 허용
                                .anyRequest().authenticated() // 그 외의 모든 요청은 인증을 요구
                )
//                .formLogin(formLogin -> formLogin // 폼 로그인 설정을 구성
//                        .loginPage("user/login") // 사용자 정의 로그인 페이지 경로를 설정
//                        .permitAll() // 로그인 페이지에 대한 접근을 허용
//                )
                .logout(logout -> logout // 로그아웃 설정을 구성
                         .permitAll() // 로그아웃 URL에 대한 접근을 허용
                )
                .csrf(csrf -> csrf.disable());
        return http.build(); // HTTP 보안 설정을 빌드하여 반환
    }

    // 비밀번호 인코더를 정의하는 빈
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCryptPasswordEncoder를 반환. 이는 비밀번호를 암호화하는 데 사용됨
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
