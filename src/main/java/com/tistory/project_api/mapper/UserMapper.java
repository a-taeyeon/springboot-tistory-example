package com.tistory.project_api.mapper;

import com.tistory.project_api.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<UserDto.UserBase> findAll();

    int signUp(UserDto.SignUp param);

    UserDto.UserBase findByEmail(UserDto.UserSearchByEmailCondition param);


    // oauth2 소셜 로그인 기반 회원가입
    int socialSignUp(UserDto.SocialSignUp param);
}
