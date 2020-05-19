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
    public String addUser(AddUserRequest addUserRequest) {
        return userStorage.addUser(addUserRequest);
    }
}
