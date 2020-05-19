package com.example.service.interfaces;

import com.example.common.pojo.AddUserRequest;

public interface UserStorage {
    String addUser(AddUserRequest addUserRequest);
}
