package com.tistory.project_api.controller;

import com.tistory.project_api.controller.request.UserRequest;
import com.tistory.project_api.dto.UserDto;
import com.tistory.project_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    private final String EP_LIST_USER = "/list";
    private final String EP_ADD_USER = "/signup";
    private final String EP_LOGIN = "/login";

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

    @GetMapping(EP_LOGIN)
    public String login(@RequestBody UserRequest.LoginRequest body) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return "User " + userDetails.getUsername() + " 님 성공적으로 로그인되었습니다.";
    }

}
