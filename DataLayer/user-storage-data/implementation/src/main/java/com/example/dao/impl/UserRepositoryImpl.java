package com.example.dao.impl;

import com.example.dao.interfaces.UserRepository;
import com.example.common.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    @Override
    public boolean existsByUsername(String username) {
        return users.entrySet().stream().anyMatch(entry -> entry.getValue().getUsername().equals(username));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findAny();
    }

    @Override
    public User findById(long id) {
        return users.get(id);
    }
}
