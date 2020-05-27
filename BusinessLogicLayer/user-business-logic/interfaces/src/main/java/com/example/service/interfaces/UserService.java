package com.example.service.interfaces;

import com.example.common.pojo.*;

import java.util.List;

public interface UserService {
    Response<?> addUser(AddUserRequest addUserRequest);
    Response<?> getAuthenticationToken(AuthenticateUserRequest authenticateUserRequest);
    Response<?> getUserInfo(String token);
    List<UserInfo> getUsersByPhrase(String phrase);
}
