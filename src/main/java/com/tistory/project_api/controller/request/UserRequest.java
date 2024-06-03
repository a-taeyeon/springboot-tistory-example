package com.tistory.project_api.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class UserRequest {
    @Setter
    @Getter
    @ToString(callSuper = true)
    public static class SignUpRequest {
        private String username;
        private String password;
        private String email;
    }
}
