package com.tistory.framework.security.config;

import com.tistory.framework.security.handler.CustomAuthenticationSuccessHandler;
import com.tistory.framework.security.service.CustomOAuth2UserService;
import com.tistory.framework.security.filter.JwtRequestFilter;
import com.tistory.framework.security.service.CustomUserDetailsService;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Spring 설정 클래스임
@EnableWebSecurity // Spring Security를 활성화
public class SecurityConfig {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    // HTTP 보안 설정을 구성하는 메서드
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> // HTTP 요청에 대한 보안 규칙을 정의
                        authorizeRequests
                                .requestMatchers("/user/signup", "/user/login",
                                        "/user/signup/jpa").permitAll() // 회원가입, 로그인에 대한 접근 허용
                                .anyRequest().authenticated() // 그 외의 모든 요청은 인증을 요구
                )
//                .httpBasic(withDefaults()) // HTTP Basic 인증 활성화 -> 주로 간단한 테스트를 위해 사용됨. JWT토큰 인증을 사용하면 없어도 됨
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class) // JWT 필터 추가
                .oauth2Login(oauth2 -> oauth2 // OAuth2 로그인 기능 활성화
                        .userInfoEndpoint(userInfo -> userInfo // 사용자 정보 엔드포인트 설정
                                .userService(customOAuth2UserService)) // 사용자 정보 로드,저장,업데이트
                        .successHandler(customAuthenticationSuccessHandler)) // OAuth2 로그인 성공 후 수행 동작 정의
                .cors(cors -> cors.disable()) // cors 비활성화: 외부에서 해당 애플리케이션 리소스에 접근할 수 있게 해주는 보안 기능
                .csrf(csrf -> csrf.disable());
        return http.build(); // HTTP 보안 설정을 빌드하여 반환
    }

    // 비밀번호 인코더를 정의
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCryptPasswordEncoder를 반환. 이는 비밀번호를 암호화하는 데 사용됨
    }

    // Spring Security의 인증 관리자. 사용자의 인증을 관리
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService) // 사용자 정보를 로드
                .passwordEncoder(passwordEncoder());    // 비밀번호 검증
        return authenticationManagerBuilder.build();
    }

}
