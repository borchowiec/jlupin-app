package com.example.common.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserInfo implements Serializable {
    private String id;
    private String username;

    public UserInfo(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
}
