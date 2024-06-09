package com.tistory.project_api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class UserDto {
    @Data
    public static class UserBase {
        private String id;
        private String username;
        @JsonIgnore
        private String password;
        private String email;
        private boolean enabled;
        private String role;
        private String createdAt;
        private String updatedAt;
    }

    @Data
    public static class UserSocialBase {
        private String id;
        private String username;
        private String email;
        private boolean enabled;
        private String role;
        private String createdAt;
        private String updatedAt;
    }

    @Data
    public static class SignUp extends UserBase{
        @Builder
        public SignUp(String id,
                      String username,
                      String password,
                      String email,
                      boolean enabled,
                      String role,
                      String createdAt,
                      String updatedAt) {
            setId(id);
            setUsername(username);
            setPassword(password);
            setEmail(email);
            setEnabled(enabled);
            setRole(role);
            setCreatedAt(createdAt);
            setUpdatedAt(updatedAt);
        }
    }

    @Data
    @Builder
    public static class UserSearchByEmailCondition {
        private String email;
    }

}
