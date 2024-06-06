package com.tistory.framework.security.utils;

import com.tistory.framework.security.dto.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
    // 고정된 비밀 키 사용 ( TODO: 환경변수 처리하기 )
    private static final String SECRET_KEY = "your_very_secret_key_which_is_very_long_and_secure";

    // JWT 토큰에서 이메일을 추출
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject); // subject를 이메일로 사용
    }

    // JWT 토큰에서 만료 시간을 추출
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // JWT 토큰에서 특정 클레임을 추출
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // JWT 토큰에서 모든 클레임을 추출
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes()).build().parseClaimsJws(token).getBody();
    }

    // JWT 토큰이 만료되었는지 확인
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 사용자 정보를 기반으로 JWT 토큰 생성
    public String generateToken(CustomUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getEmail());
    }

    // 클레임과 주제를 기반으로 JWT 토큰 생성
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24시간 동안 유효
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes()).compact();
    }

    // JWT 토큰의 유효성 검증
    public Boolean validateToken(String token, CustomUserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getEmail()) && !isTokenExpired(token));
    }
}
