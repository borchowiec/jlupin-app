package com.example.service.impl;

import com.example.common.pojo.*;
import com.example.common.util.JwtTokenProvider;
import com.example.service.interfaces.UserService;
import com.example.service.interfaces.UserStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "userService")
public class UserServiceImpl implements UserService {
    @Autowired
    @Qualifier("userStorage")
    private UserStorage userStorage;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private JwtTokenProvider tokenProvider = JwtTokenProvider.getInstance();

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

    @Override
    public boolean isTokenValid(String token) {
        return tokenProvider.isValid(token);
    }

    @Override
    public UserInfo getUserInfo(String token) {
        String id = tokenProvider.getId(token);
        User user = userStorage.getUser(id);
        return new UserInfo(user);
    }

    @Override
    public List<UserInfo> getUsersByPhrase(String phrase) {
        return userStorage.getUsersByPhrase(phrase);
    }
}
