package com.tistory.project_api.controller.request;

import lombok.Data;

public class UserRequest {
    @Data
    public static class SignUpRequest {
        private String username;
        private String password;
        private String email;
    }
}
