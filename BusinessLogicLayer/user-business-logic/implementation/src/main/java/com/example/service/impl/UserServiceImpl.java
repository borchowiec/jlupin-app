package com.example.service.impl;

import com.example.common.pojo.*;
import com.example.common.util.JwtTokenProvider;
import com.example.service.interfaces.UserService;
import com.example.service.interfaces.UserStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
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
    public Response<?> addUser(AddUserRequest addUserRequest) {
        // check if user already exists
        String username = addUserRequest.getUsername();
        boolean alreadyExists = userStorage.existsByUsername(username);
        if (alreadyExists) {
            return new Response<>(new ErrorMessage(String.format("User of username: %s already exists.", username)),
                    HttpStatus.BAD_REQUEST);
        }

        // encode password
        addUserRequest.setPassword(passwordEncoder.encode(addUserRequest.getPassword()));
        userStorage.addUser(addUserRequest);
        return new Response<>(true, HttpStatus.OK);
    }

    @Override
    public Response<?> getAuthenticationToken(AuthenticateUserRequest request) {
        // get user
        String username = request.getUsername();
        User user = userStorage.findByUsername(username);
        if (user == null) {
            return new Response<>(new ErrorMessage("Wrong credentials."), HttpStatus.FORBIDDEN);
        }

        // check password
        boolean validPassword = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!validPassword) {
            return new Response<>(new ErrorMessage("Wrong credentials."), HttpStatus.FORBIDDEN);
        }

        // generate token
        AuthenticateUserResponse response = new AuthenticateUserResponse();
        String token = tokenProvider.generateToken(user);
        response.setToken(token);
        return new Response<>(response, HttpStatus.OK);
    }

    @Override
    public Response<?> getUserInfo(String token) {
        String id = tokenProvider.getId(token);
        if (!userStorage.existsById(id)) {
            String msg = String.format("Not found user of id %s. Try to log out and log in.", id);
            return new Response<>(new ErrorMessage(msg), HttpStatus.FORBIDDEN);
        }
        User user = userStorage.getUser(id);
        return new Response<>(new UserInfo(user), HttpStatus.OK);
    }

    @Override
    public List<UserInfo> getUsersByPhrase(String phrase) {
        return userStorage.getUsersByPhrase(phrase);
    }
}
