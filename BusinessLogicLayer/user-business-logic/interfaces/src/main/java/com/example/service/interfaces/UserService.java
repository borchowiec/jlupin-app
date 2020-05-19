package com.example.service.interfaces;

import com.example.common.pojo.AddUserRequest;
import com.example.common.pojo.AuthenticateUserRequest;

public interface UserService {
    boolean addUser(AddUserRequest addUserRequest);
    String getAuthenticationToken(AuthenticateUserRequest authenticateUserRequest);
}
