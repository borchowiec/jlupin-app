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
import java.util.UUID;

@Repository(value = "userRepository")
public class UserRepositoryImpl implements UserRepository {

    private Map<String, User> users = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @PostConstruct
    public void addExampleUsers() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        insert(new User(UUID.randomUUID().toString(), "admin", passwordEncoder.encode("password")));
        insert(new User(UUID.randomUUID().toString(), "username123", passwordEncoder.encode("password")));
        insert(new User(UUID.randomUUID().toString(), "userr", passwordEncoder.encode("password")));
        insert(new User(UUID.randomUUID().toString(), "admin3", passwordEncoder.encode("password")));
    }

    @Override
    public User insert(User user) {
        User copy = new User(user);
        String id = UUID.randomUUID().toString();
        copy.setId(id);
        users.put(copy.getId(), copy);
        logger.info(users.toString());
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
    public User findById(String id) {
        return users.get(id);
    }
}
