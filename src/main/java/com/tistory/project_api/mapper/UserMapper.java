package com.tistory.project_api.mapper;

import com.tistory.project_api.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<UserDto> findAll();
}
