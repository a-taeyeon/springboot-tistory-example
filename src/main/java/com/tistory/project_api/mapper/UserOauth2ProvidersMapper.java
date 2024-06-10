package com.tistory.project_api.mapper;

import com.tistory.project_api.domain.UserOauth2Providers;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserOauth2ProvidersMapper {
    int insertUserProvider(UserOauth2Providers.AddUserOauth2Provider param);

    UserOauth2Providers.UserOauth2ProviderBase findByEmailProvider(UserOauth2Providers.UserProviderCondition param);
}
