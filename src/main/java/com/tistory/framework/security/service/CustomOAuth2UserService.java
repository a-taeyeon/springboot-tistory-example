package com.tistory.framework.security.service;

import com.tistory.framework.security.dto.CustomOAuth2User;
import com.tistory.framework.security.dto.CustomUserDetails;
import com.tistory.framework.security.utils.JwtTokenUtil;
import com.tistory.project_api.dto.UserDto;
import com.tistory.project_api.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // OAuth2 로그인 후 사용자 정보를 로드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 구글 OAuth2 제공자 ID 가져오기
        String oauth2Provider = userRequest.getClientRegistration().getRegistrationId();
        String oauth2ProviderId = oAuth2User.getName();

        // 사용자 정보를 가져와서 데이터베이스에 저장
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // 사용자 정보 저장 또는 업데이트
        UserDto.UserBase user = userMapper.findByEmail(UserDto.UserSearchByEmailCondition.builder()
                .email(email).build());
        if (user == null) {
            UserDto.SocialSignUp dto = UserDto.SocialSignUp.builder()
                    .username(name)
                    .email(email)
                    .oauth2Provider(oauth2Provider)
                    .oauth2ProviderId(oauth2ProviderId)
                    .build();
            userMapper.socialSignUp(dto);
        } else {
            // 기존 사용자 업데이트

        }

        // JWT 토큰 생성
        CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        String jwtToken = jwtTokenUtil.generateToken(userDetails);

        // JWT 토큰을 사용자 정보에 추가
        return new CustomOAuth2User(oAuth2User, jwtToken);
    }
}
