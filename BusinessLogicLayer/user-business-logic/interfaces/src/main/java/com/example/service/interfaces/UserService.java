package com.example.service.interfaces;

import com.example.common.pojo.AddUserRequest;
import com.example.common.pojo.AuthenticateUserRequest;
import com.example.common.pojo.AuthenticateUserResponse;
import com.example.common.pojo.UserInfo;

public interface UserService {
    boolean addUser(AddUserRequest addUserRequest);
    AuthenticateUserResponse getAuthenticationToken(AuthenticateUserRequest authenticateUserRequest);
    boolean isTokenValid(String token);
    UserInfo getUserInfo(String token);
}
