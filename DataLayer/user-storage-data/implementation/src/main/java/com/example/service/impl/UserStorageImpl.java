package com.example.service.impl;

import com.example.common.pojo.AddUserRequest;
import com.example.common.pojo.User;
import com.example.dao.interfaces.UserRepository;
import com.example.service.interfaces.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service(value = "userStorage")
public class UserStorageImpl implements UserStorage {
    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Override
    public boolean addUser(AddUserRequest addUserRequest) {
        User user = new User();
        user.setPassword(addUserRequest.getPassword());
        user.setUsername(addUserRequest.getUsername());

        User put = userRepository.put(user);
        return put != null;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Wrong credentials"));
    }

    @Override
    public List<User> findByIds(long... ids) {
        List<User> users = new LinkedList();
        for (long id : ids) {
            users.add(userRepository.findById(id));
        }
        return users;
    }
}
