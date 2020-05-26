package com.example.common.util;

import com.example.common.pojo.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {
    @Test
    void test() {
        JwtTokenProvider instance = JwtTokenProvider.getInstance();
        User user = new User();
        user.setId("someid");

        String token = instance.generateToken(user);
        String actual = instance.getId("Bearer " + token);

        assertEquals(user.getId(), actual);
    }
}