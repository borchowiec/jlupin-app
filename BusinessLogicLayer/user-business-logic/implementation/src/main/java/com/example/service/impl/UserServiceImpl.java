package com.example.service.impl;

import com.example.bean.interfaces.TokenProvider;
import com.example.common.pojo.AddUserRequest;
import com.example.common.pojo.AuthenticateUserRequest;
import com.example.common.pojo.AuthenticateUserResponse;
import com.example.common.pojo.User;
import com.example.service.interfaces.UserService;
import com.example.service.interfaces.UserStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service(value = "userService")
public class UserServiceImpl implements UserService {
    @Autowired
    @Qualifier("userStorage")
    private UserStorage userStorage;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("tokenProvider")
    private TokenProvider tokenProvider;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public boolean addUser(AddUserRequest addUserRequest) {
        // check if user already exists
        String username = addUserRequest.getUsername();
        boolean alreadyExists = userStorage.existsByUsername(username);
        if (alreadyExists) {
            String message = "User of username: " + username + " already exists.";
            return false;
        }

        // encode password
        addUserRequest.setPassword(passwordEncoder.encode(addUserRequest.getPassword()));

        return userStorage.addUser(addUserRequest);
    }

    @Override
    public AuthenticateUserResponse getAuthenticationToken(AuthenticateUserRequest request) {
        // get user
        String username = request.getUsername();
        User user = userStorage.findByUsername(username);

        // check password
        boolean validPassword = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!validPassword) {
            throw new RuntimeException("Wrong credentials");
        }

        // generate token
        AuthenticateUserResponse response = new AuthenticateUserResponse();
        String token = tokenProvider.generateToken(user);
        response.setToken(token);
        return response;
    }
}
