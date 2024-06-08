package com.tistory.framework.security.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * OAuth2 인증 후에 사용자 정보와 JWT 토큰을 포함하는 커스텀 OAuth2User 객체
 */
public class CustomOAuth2User implements OAuth2User {
    private final OAuth2User oAuth2User;
    private final String jwtToken;

    public CustomOAuth2User(OAuth2User oAuth2User, String jwtToken) {
        this.oAuth2User = oAuth2User;
        this.jwtToken = jwtToken;
    }

    // 기존 OAuth2User의 속성에 JWT 토큰을 추가하여 반환
    @Override
    public Map<String, Object> getAttributes() {
        // 기존 OAuth2User의 속성을 가져와서 새로운 HashMap에 복사
        Map<String, Object> attributes = new java.util.HashMap<>(oAuth2User.getAttributes());
        // JWT 토큰을 추가
        attributes.put("jwtToken", jwtToken);
        return attributes;
    }

    // 사용자의 권한을 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 기존 OAuth2User의 권한을 반환
        return oAuth2User.getAuthorities();
    }

    // 사용자의 이름을 반환
    @Override
    public String getName() {
        return oAuth2User.getName();
    }

    // JWT 토큰을 반환
    public String getJwtToken() {
        return jwtToken;
    }
}
