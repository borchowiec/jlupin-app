package com.example.service.impl;

import com.example.common.pojo.AddUserRequest;
import com.example.service.interfaces.UserService;
import com.example.service.interfaces.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service(value = "userService")
public class UserServiceImpl implements UserService {
    @Autowired
    @Qualifier("userStorage")
    private UserStorage userStorage;

    @Override
    public boolean addUser(AddUserRequest addUserRequest) {
        // check if user already exists
        String username = addUserRequest.getUsername();
        boolean alreadyExists = userStorage.existsByUsername(username);
        if (alreadyExists) {
            String message = "User of username: " + username + " already exists.";
            return false;
        }

        return userStorage.addUser(addUserRequest);
    }
}
