package com.tistory.project_api.controller;

import com.tistory.framework.core.response.BaseResponse;
import com.tistory.framework.security.domain.CustomUserDetails;
import com.tistory.framework.security.utils.JwtTokenUtil;
import com.tistory.project_api.controller.request.UserRequest;
import com.tistory.project_api.controller.response.UserResponse;
import com.tistory.project_api.domain.User;
import com.tistory.project_api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    private final String EP_LIST_USER = "/list";
    private final String EP_ADD_USER = "/signup";
    private final String EP_LOGIN = "/login";
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(EP_LIST_USER)
    public BaseResponse<UserResponse.UserList> getAllUsers() {
        BaseResponse<UserResponse.UserList> res = new BaseResponse<>();

        List<User.UserBase> userListDto = userService.findAll();

        if(userListDto != null){
            UserResponse.UserList userListResponse = new UserResponse.UserList();
            userListResponse.setTotal(userListDto.size());

            ModelMapper modelMapper = new ModelMapper();
            List<UserResponse.UserDetail> userDetails = userListDto.stream()
                    .map(user -> modelMapper.map(user, UserResponse.UserDetail.class))
                    .collect(Collectors.toList());
            userListResponse.setUserList(userDetails);

            res.setResult(userListResponse);
        }
        return res;
    }

    @PostMapping(EP_ADD_USER)
    public User.SignUp addUser(@RequestBody UserRequest.SignUpRequest body) {
        return userService.signUp(body);
    }

    @GetMapping(EP_LOGIN)
    public String login(@RequestBody UserRequest.LoginRequest body) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String jwtToken = jwtTokenUtil.generateToken(userDetails);

        return "Bearer  " + jwtToken;
    }

}
