package com.tistory.project_api.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tistory.project_api.domain.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class UserResponse {
    @Data
    public static class UserList {
        private int total;
        private List<UserDetail> userList;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDetail {
        private String id;
        private String username;
        @JsonIgnore
        private String password;
        private String email;
        private boolean enabled;
        private String role;
        private String createdAt;
        private String updatedAt;

        public UserDetail(UserEntity userEntity) {
            this.id = userEntity.getId();
            this.username = userEntity.getUsername();
            this.password = userEntity.getPassword();
            this.email = userEntity.getEmail();
            this.enabled = userEntity.isEnabled();
            this.role = userEntity.getRole();
            this.createdAt = userEntity.getCreatedAt();
            this.updatedAt = userEntity.getUpdatedAt();
        }
    }
}
