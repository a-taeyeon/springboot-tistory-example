package com.tistory.project_api.service;

import com.tistory.project_api.controller.request.UserRequest;
import com.tistory.project_api.mapper.UserMapper;
import com.tistory.project_api.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public List<UserDto.UserBase> findAll() {
        List<UserDto.UserBase> result = userMapper.findAll();
        return result;
    }

    public UserDto.SignUp signUp(UserRequest.SignUpRequest param) {
        UserDto.SignUp dto = UserDto.SignUp.builder()
                .username(param.getUsername())
                .password(param.getPassword())
                .email(param.getEmail())
                .build();

        userMapper.signUp(dto);

        return dto;
    }
}
