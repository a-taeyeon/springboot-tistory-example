package com.tistory.project_api.mapper;

import com.tistory.project_api.dto.UserOauth2ProvidersDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserOauth2ProvidersMapper {
    int insertUserProvider(UserOauth2ProvidersDto.AddUserOauth2Provider param);

    UserOauth2ProvidersDto.UserOauth2ProviderBase findByEmailProvider(UserOauth2ProvidersDto.UserProviderCondition param);
}
