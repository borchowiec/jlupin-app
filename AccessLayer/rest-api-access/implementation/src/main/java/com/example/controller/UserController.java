package com.example.controller;

import com.example.common.pojo.AddUserRequest;
import com.example.common.pojo.AuthenticateUserRequest;
import com.example.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public String authenticateUser(@RequestBody AuthenticateUserRequest request) {
        return userService.getAuthenticationToken(request);
    }
}
