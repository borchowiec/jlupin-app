package com.example.service.impl;

import com.example.common.pojo.AddUserRequest;
import com.example.common.pojo.User;
import com.example.common.pojo.UserInfo;
import com.example.dao.interfaces.UserRepository;
import com.example.service.interfaces.UserStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "userStorage")
public class UserStorageImpl implements UserStorage {
    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserStorageImpl.class);

    @Override
    public boolean addUser(AddUserRequest addUserRequest) {
        User user = new User();
        user.setPassword(addUserRequest.getPassword());
        user.setUsername(addUserRequest.getUsername());

        User put = userRepository.insert(user);
        return put != null;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsById(String userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElse(null);
    }

    @Override
    public List<User> findByIds(String... ids) {
        List<User> users = new LinkedList();
        for (String id : ids) {
            users.add(userRepository.findById(id));
        }
        return users;
    }

    @Override
    public User getUser(String userId) {
        User byId = userRepository.findById(userId);
        return byId;
    }

    @Override
    public List<UserInfo> getUsersByPhrase(String phrase) {
        return userRepository
                .findUsersByPhrase(phrase)
                .stream()
                .map(UserInfo::new)
                .collect(Collectors.toList());
    }
}
