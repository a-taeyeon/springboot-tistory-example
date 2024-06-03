package com.tistory.project_api.controller;

import com.tistory.project_api.controller.request.UserRequest;
import com.tistory.project_api.dto.UserDto;
import com.tistory.project_api.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final String EP_LIST_USER = "/list";
    private final String EP_ADD_USER = "/signup";

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(EP_LIST_USER)
    public List<UserDto.UserBase> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping(EP_ADD_USER)
    public UserDto.SignUp addUser(@RequestBody UserRequest.SignUpRequest body) {
        return userService.signUp(body);
    }

}
