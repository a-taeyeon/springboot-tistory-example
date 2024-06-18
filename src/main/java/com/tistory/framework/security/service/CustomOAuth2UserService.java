package com.tistory.framework.security.service;

import com.tistory.framework.security.domain.CustomOAuth2User;
import com.tistory.framework.security.domain.CustomUserDetails;
import com.tistory.framework.security.utils.JwtTokenUtil;
import com.tistory.project_api.domain.User;
import com.tistory.project_api.domain.UserOauth2Providers;
import com.tistory.project_api.mapper.UserMapper;
import com.tistory.project_api.mapper.UserOauth2ProvidersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserOauth2ProvidersMapper userOauth2ProvidersMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";
    private static final String GOOGLE = "google";
    private static final String RESPONSE = "response";
    private static final String KAKAO_ACCOUNT = "kakao_account";
    private static final String PROFILE = "profile";
    private static final String EMAIL = "email";
    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String NICKNAME = "nickname";

    /**
     * OAuth2 로그인 후 사용자 정보를 로드
     * @param userRequest the OAuth2 user request
     * @return the OAuth2User with JWT token
     * @throws OAuth2AuthenticationException if an authentication error occurs
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        UserAttributes userAttributes = extractUserAttributes(userRequest, oAuth2User);
        processUser(userAttributes);
        CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(userAttributes.getEmail());
        String jwtToken = jwtTokenUtil.generateToken(userDetails);
        return new CustomOAuth2User(oAuth2User, jwtToken);
    }

    /**
     * 사용자 요청과 OAuth2 사용자로부터 사용자 속성을 추출
     * @param userRequest the OAuth2 user request
     * @param oAuth2User the OAuth2 user
     * @return the extracted UserAttributes
     */
    private UserAttributes extractUserAttributes(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String oauth2Provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        switch (oauth2Provider) {
            case NAVER:
                return extractNaverAttributes(attributes);
            case KAKAO:
                return extractKakaoAttributes(attributes, oAuth2User);
            case GOOGLE:
                return extractGoogleAttributes(attributes, oAuth2User);
            default:
                throw new OAuth2AuthenticationException("Unsupported OAuth2 provider: " + oauth2Provider);
        }
    }

    /**
     * 네이버 사용자 속성을 추출
     * @param attributes the OAuth2 user attributes
     * @return the extracted UserAttributes
     */
    private UserAttributes extractNaverAttributes(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get(RESPONSE);
        return new UserAttributes(
                (String) response.get(EMAIL),
                (String) response.get(NAME),
                NAVER,
                (String) response.get(ID)
        );
    }

    /**
     * 카카오 사용자 속성을 추출
     * @param attributes the OAuth2 user attributes
     * @param oAuth2User the OAuth2 user
     * @return the extracted UserAttributes
     */
    private UserAttributes extractKakaoAttributes(Map<String, Object> attributes, OAuth2User oAuth2User) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get(KAKAO_ACCOUNT);
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get(PROFILE);
        return new UserAttributes(
                (String) kakaoAccount.get(EMAIL),
                (String) kakaoProfile.get(NICKNAME),
                KAKAO,
                oAuth2User.getName()
        );
    }

    /**
     * 구글 사용자 속성을 추출
     * @param attributes the OAuth2 user attributes
     * @param oAuth2User the OAuth2 user
     * @return the extracted UserAttributes
     */
    private UserAttributes extractGoogleAttributes(Map<String, Object> attributes, OAuth2User oAuth2User) {
        return new UserAttributes(
                (String) attributes.get(EMAIL),
                (String) attributes.get(NAME),
                GOOGLE,
                oAuth2User.getName()
        );
    }

    /**
     * 사용자 정보 처리 (생성 or 업데이트 or 통합)
     * @param userAttributes the user attributes
     */
    @Transactional(readOnly = true)
    public void processUser(UserAttributes userAttributes) {
        User.UserBase user = userMapper.findByEmail(User.UserSearchByEmailCondition.builder()
                .email(userAttributes.getEmail())
                .build());

        if (user == null) {
            registerNewUser(userAttributes);
        } else {
            checkAndAddOauthProvider(userAttributes);
        }
    }

    /**
     * 새로운 사용자를 등록
     * @param userAttributes the user attributes
     */
    private void registerNewUser(UserAttributes userAttributes) {
        User.SignUp signUpDto = User.SignUp.builder()
                .username(userAttributes.getName())
                .email(userAttributes.getEmail())
                .build();
        userMapper.signUp(signUpDto);

        addUserProvider(userAttributes);
    }

    /**
     * 사용자가 기존에 등록되어 있는지 확인하고 OAuth2 provider를 추가
     * @param userAttributes the user attributes
     */
    private void checkAndAddOauthProvider(UserAttributes userAttributes) {
        UserOauth2Providers.UserOauth2ProviderBase existUser = userOauth2ProvidersMapper.findByEmailProvider(
                UserOauth2Providers.UserProviderCondition.builder()
                        .email(userAttributes.getEmail())
                        .provider(userAttributes.getOauth2Provider())
                        .build());

        if (existUser == null) {
            addUserProvider(userAttributes);
        }
    }

    /**
     * OAuth2 provider를 사용자에게 추가
     * @param userAttributes the user attributes
     */
    private void addUserProvider(UserAttributes userAttributes) {
        UserOauth2Providers.AddUserOauth2Provider providerDto = UserOauth2Providers.AddUserOauth2Provider.builder()
                .email(userAttributes.getEmail())
                .provider(userAttributes.getOauth2Provider())
                .providerId(userAttributes.getOauth2ProviderId())
                .build();
        userOauth2ProvidersMapper.insertUserProvider(providerDto);
    }

    /**
     * 사용자 속성을 캡슐화하는 클래스
     */
    private static class UserAttributes {
        private final String email;
        private final String name;
        private final String oauth2Provider;
        private final String oauth2ProviderId;

        public UserAttributes(String email, String name, String oauth2Provider, String oauth2ProviderId) {
            this.email = email;
            this.name = name;
            this.oauth2Provider = oauth2Provider;
            this.oauth2ProviderId = oauth2ProviderId;
        }

        public String getEmail() {
            return email;
        }

        public String getName() {
            return name;
        }

        public String getOauth2Provider() {
            return oauth2Provider;
        }

        public String getOauth2ProviderId() {
            return oauth2ProviderId;
        }
    }
}
