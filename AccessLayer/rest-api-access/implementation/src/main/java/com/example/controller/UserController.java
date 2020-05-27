package com.example.controller;

import com.example.common.pojo.*;
import com.example.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    @Qualifier("userService")
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/add-user")
    public ResponseEntity<?> addUser(@RequestBody @Valid AddUserRequest request) {
        Response<?> response = userService.addUser(request);
        return new ResponseEntity<>(response.getPayload(), response.getStatus());
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthenticateUserRequest request) {
        Response<?> response = userService.getAuthenticationToken(request);
        return new ResponseEntity<>(response.getPayload(), response.getStatus());
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {
        Response<?> response = userService.getUserInfo(token);
        return new ResponseEntity<>(response.getPayload(), response.getStatus());
    }

    @GetMapping("/users/by-phrase/{phrase}")
    public List<UserInfo> getUsersByPhrase(@PathVariable String phrase) {
        return userService.getUsersByPhrase(phrase);
    }
}
