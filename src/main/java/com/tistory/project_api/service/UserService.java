package com.tistory.project_api.service;

import com.tistory.project_api.controller.request.UserRequest;
import com.tistory.project_api.controller.response.UserResponse;
import com.tistory.project_api.domain.entity.UserEntity;
import com.tistory.project_api.mapper.UserMapper;
import com.tistory.project_api.domain.User;
import com.tistory.project_api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserMapper userMapper,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


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

    /**
     * jpa
     */
    public List<UserEntity> findAllUsers(){
        return userRepository.findAll();
    }

    public UserResponse.UserDetail saveUser(UserRequest.SignUpRequest param){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(param.getUsername());
        userEntity.setPassword(passwordEncoder.encode(param.getPassword()));
        userEntity.setEmail(param.getEmail());

        UserEntity savedUser = userRepository.save(userEntity);
        return new UserResponse.UserDetail(savedUser);
    }
}
