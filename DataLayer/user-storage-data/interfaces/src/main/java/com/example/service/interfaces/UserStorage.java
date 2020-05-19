package com.example.service.interfaces;

import com.example.common.pojo.AddUserRequest;

public interface UserStorage {
    boolean addUser(AddUserRequest addUserRequest);
    boolean existsByUsername(String username);
}
