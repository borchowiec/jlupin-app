package com.example.common.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddUserRequest implements Serializable {
    private String username;
    private String password;
}
