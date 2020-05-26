package com.example.dao.interfaces;

import com.example.common.pojo.User;

import java.util.Optional;

public interface UserRepository {
    User insert(User user);

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    User findById(String id);
}
