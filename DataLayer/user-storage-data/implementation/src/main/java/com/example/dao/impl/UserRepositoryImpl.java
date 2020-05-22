package com.example.dao.impl;

import com.example.common.pojo.User;
import com.example.dao.interfaces.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository(value = "userRepository")
public class UserRepositoryImpl implements UserRepository {

    private Map<Long, User> users = new HashMap<>();
    private static long nextId = 0L;
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @PostConstruct
    public void addExampleUsers() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        put(new User(0, "admin", passwordEncoder.encode("password")));
        put(new User(0, "username123", passwordEncoder.encode("password")));
        put(new User(0, "userr", passwordEncoder.encode("password")));
        put(new User(0, "admin3", passwordEncoder.encode("password")));
    }

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
