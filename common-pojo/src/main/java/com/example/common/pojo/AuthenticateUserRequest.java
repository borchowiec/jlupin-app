package com.example.common.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthenticateUserRequest implements Serializable {
    private String username;
    private String password;
}
