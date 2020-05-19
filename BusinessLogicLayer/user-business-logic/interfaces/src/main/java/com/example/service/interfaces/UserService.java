package com.example.service.interfaces;

import com.example.common.pojo.AddUserRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    boolean addUser(AddUserRequest addUserRequest);
}
