package com.example.service.interfaces;

import com.example.common.pojo.AddUserRequest;
import com.example.common.pojo.User;

public interface UserStorage {
    boolean addUser(AddUserRequest addUserRequest);
    boolean existsByUsername(String username);
    User findByUsername(String username);
}
