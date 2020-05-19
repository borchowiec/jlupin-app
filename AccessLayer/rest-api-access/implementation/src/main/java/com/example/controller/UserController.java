package com.example.controller;

import com.example.common.pojo.AddUserRequest;
import com.example.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @PostMapping("/add-user")
    public String getTest(@RequestBody AddUserRequest addUserRequest) {
        return userService.addUser(addUserRequest);
    }
}
