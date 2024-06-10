package com.tistory.project_api.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserOauth2Providers {

    @Data
    public static class UserOauth2ProviderBase {
        private String email;
        private String provider;
        private String providerId;
        private String createdAt;
        private String updatedAt;
    }

    @Data
    public static class AddUserOauth2Provider extends UserOauth2ProviderBase{
        @Builder
        public AddUserOauth2Provider(String email,
                                     String provider,
                                     String providerId,
                                     String createdAt,
                                     String updatedAt) {
            setEmail(email);
            setProvider(provider);
            setProviderId(providerId);
            setCreatedAt(createdAt);
            setUpdatedAt(updatedAt);
        }

    }



    @Data
    public static class UserProviderCondition {
        private String email;
        private String provider;
        @Builder
        public UserProviderCondition(String email,
                                     String provider) {
            setEmail(email);
            setProvider(provider);
        }
    }
}
