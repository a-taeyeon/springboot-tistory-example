package com.tistory.framework.service;

import com.tistory.framework.dto.CustomUserDetails;
import com.tistory.project_api.dto.UserDto;
import com.tistory.project_api.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// 사용자 정보를 데이터베이스에서 로드하고, 이를 CustomUserDetails로 변환하여 반환하는 역할
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired // UserMapper 빈을 자동으로 주입
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.error("!!!!!!!!!!!!!!!!!!!!!!loadUserByUsername");
        // 데이터베이스에서 이메일을 기반으로 사용자 정보를 조회
        UserDto.UserBase user = userMapper.findByEmail(UserDto.UserSearchByEmailCondition.builder()
                .email(email)
                .build());
        if(user == null){
            // 사용자가 없을 경우 예외처리
            throw new UsernameNotFoundException(email + " 이메일로 가입된 사용자가 없습니다");
        }

        // 사용자의 권한 목록을 생성
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("user"));

        log.error("!!!!!!!!!!!!!!!!!!" + authorities);
        log.error("@@@@ user: " + user);
        // CustomUserDetails 객체를 생성하여 반환
        CustomUserDetails customUserDetails = new CustomUserDetails(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), authorities, user.isEnabled());
        log.error("@@@@ customUserDetails: " + customUserDetails.toString());
        return customUserDetails;
    }
}
