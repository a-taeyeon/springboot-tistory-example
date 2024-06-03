package com.tistory.project_api.dto;

import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    @Getter
    @Setter
    @ToString
    public static class UserBase {
        private String id;
        private String username;
        private String password;
        private String email;
        private String createdAt;
        private String updatedAt;
    }


    @Getter
    @Setter
    @ToString(callSuper = true)
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
