package com.tistory.project_api.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    @Data
    public static class UserList {
        private int total;
        private List<UserDetail> userList;
    }
    @Data
    public static class UserDetail {
        private String id;
        private String username;
        private String email;
        private boolean enabled;
        private String role;
        private String createdAt;
        private String updatedAt;
    }
}
