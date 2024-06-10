package com.tistory.project_api.service;

import com.tistory.project_api.controller.request.UserRequest;
import com.tistory.project_api.mapper.UserMapper;
import com.tistory.project_api.domain.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User.UserBase> findAll() {
        List<User.UserBase> result = userMapper.findAll();
        return result;
    }

    @Transactional
    public User.SignUp signUp(UserRequest.SignUpRequest param) {
        // 비밀번호를 BCrypt 형식으로 인코딩
        String encodedPassword = passwordEncoder.encode(param.getPassword());

        User.SignUp dto = User.SignUp.builder()
                .username(param.getUsername())
                .password(encodedPassword)
                .email(param.getEmail())
                .build();

        userMapper.signUp(dto);

        return dto;
    }

    public User.UserBase findByEmail(String email) {
        User.UserBase  dto = userMapper.findByEmail(User.UserSearchByEmailCondition.builder()
                .email(email)
                .build());

        return dto;
    }
}
