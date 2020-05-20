package com.example.common.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthenticateUserResponse implements Serializable {
    private String token;
    private String type = "Bearer";
}
