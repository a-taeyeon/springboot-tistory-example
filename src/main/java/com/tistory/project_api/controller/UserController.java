package com.tistory.project_api.controller;

import com.tistory.framework.core.response.BaseResponse;
import com.tistory.framework.security.domain.CustomUserDetails;
import com.tistory.framework.security.utils.JwtTokenUtil;
import com.tistory.project_api.controller.request.UserRequest;
import com.tistory.project_api.controller.response.UserResponse;
import com.tistory.project_api.domain.User;
import com.tistory.project_api.domain.entity.UserEntity;
import com.tistory.project_api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    private final String EP_LIST_USER = "/list";
    private final String EP_ADD_USER = "/signup";
    private final String EP_LOGIN = "/login";

    private final String EP_JPA_LIST_USER = "/list/jpa";
    private final String EP_JPA_ADD_USER = "/signup/jpa";


    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserController(UserService userService,
                          AuthenticationManager authenticationManager,
                          JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
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
    public BaseResponse<UserResponse.UserDetail> addUser(@RequestBody UserRequest.SignUpRequest body) {
        BaseResponse<UserResponse.UserDetail> res = new BaseResponse<>();
        User.UserBase dto = userService.signUp(body);
        if(dto != null){
            ModelMapper modelMapper = new ModelMapper();
            res.setResult(modelMapper.map(dto, UserResponse.UserDetail.class));
        }
        return res;
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

    /**
     * jpa
     */
    @GetMapping(EP_JPA_LIST_USER)
    public BaseResponse<UserResponse.UserList> findAllUsers() {
        BaseResponse<UserResponse.UserList> res = new BaseResponse<>();

        List<UserEntity> findUsrs = userService.findAllUsers();

        if(findUsrs != null){
            UserResponse.UserList userListResponse = new UserResponse.UserList();
            userListResponse.setTotal(findUsrs.size());

            ModelMapper modelMapper = new ModelMapper();
            List<UserResponse.UserDetail> userDetails = findUsrs.stream()
                    .map(user -> modelMapper.map(user, UserResponse.UserDetail.class))
                    .collect(Collectors.toList());
            userListResponse.setUserList(userDetails);

            res.setResult(userListResponse);
        }
        return res;
    }

    @PostMapping(EP_JPA_ADD_USER)
    public BaseResponse<UserResponse.UserDetail> createUser(@RequestBody UserRequest.SignUpRequest body) {
        BaseResponse<UserResponse.UserDetail> res = new BaseResponse<>();
        UserResponse.UserDetail createdUser = userService.saveUser(body);

        if(createdUser != null){
            res.setResult(createdUser);
        }
        return res;
    }
}
