package com.example.bean.interfaces;

import com.example.common.pojo.User;

public interface TokenProvider {
    String generateToken(User user);

    boolean isValid(String token);

    String getId(String token);
}
