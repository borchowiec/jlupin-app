package com.example.service.impl;


import com.example.common.pojo.AddUserRequest;
import com.example.service.interfaces.UserStorage;
import org.springframework.stereotype.Service;

@Service(value = "userStorage")
public class UserStorageImpl implements UserStorage {
    @Override
    public String addUser(AddUserRequest addUserRequest) {
        return addUserRequest.toString();
    }
}
