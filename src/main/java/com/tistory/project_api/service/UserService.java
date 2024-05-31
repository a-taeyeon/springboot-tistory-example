package com.tistory.project_api.service;

import com.tistory.project_api.mapper.UserMapper;
import com.tistory.project_api.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<UserDto> findAll() {
        return userMapper.findAll();
    }
}
