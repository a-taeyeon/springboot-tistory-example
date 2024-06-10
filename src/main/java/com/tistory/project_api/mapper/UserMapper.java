package com.tistory.project_api.mapper;

import com.tistory.project_api.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User.UserBase> findAll();

    int signUp(User.SignUp param);

    User.UserBase findByEmail(User.UserSearchByEmailCondition param);

}
