package com.example.controller;

import com.example.common.pojo.AddUserRequest;
import com.example.common.pojo.AuthenticateUserRequest;
import com.example.common.pojo.AuthenticateUserResponse;
import com.example.common.pojo.UserInfo;
import com.example.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {
    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @PostMapping("/add-user")
    public boolean addUser(@RequestBody @Valid AddUserRequest request) {
        return userService.addUser(request);
    }

    @PostMapping("/authenticate")
    public AuthenticateUserResponse authenticateUser(@RequestBody AuthenticateUserRequest request) {
        return userService.getAuthenticationToken(request);
    }

    @GetMapping("/user")
    public UserInfo getUserInfo(@RequestHeader("Authorization") String token) {
        return userService.getUserInfo(token);
    }
}
