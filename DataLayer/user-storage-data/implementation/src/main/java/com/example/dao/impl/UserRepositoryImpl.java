package com.example.dao.impl;

import com.example.dao.interfaces.UserRepository;
import com.example.dao.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository(value = "userRepository")
public class UserRepositoryImpl implements UserRepository {

    private Map<Long, User> users = new HashMap<>();
    private static long nextId = 0L;

    @Override
    public User put(User user) {
        User copy = new User(user);
        copy.setId(nextId);
        nextId++;
        users.put(copy.getId(), copy);
        return copy;
    }
}
