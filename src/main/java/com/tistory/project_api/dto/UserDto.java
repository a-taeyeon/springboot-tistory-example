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
                      String createdAt,
                      String updatedAt) {
            setId(id);
            setUsername(username);
            setPassword(password);
            setEmail(email);
            setCreatedAt(createdAt);
            setUpdatedAt(updatedAt);
        }
    }

}
