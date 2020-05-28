package com.example.service.interfaces;

import com.example.common.pojo.AddUserRequest;
import com.example.common.pojo.User;
import com.example.common.pojo.UserInfo;

import java.util.List;

public interface UserStorage {
    boolean addUser(AddUserRequest addUserRequest);
    boolean existsByUsername(String username);
    boolean existsById(String userId);
    User findByUsername(String username);
    List<User> findByIds(String... ids);
    User getUser(String userId);
    List<UserInfo> getUsersByPhrase(String phrase);
}
