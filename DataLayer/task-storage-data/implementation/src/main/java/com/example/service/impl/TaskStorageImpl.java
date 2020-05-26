package com.example.service.impl;

import com.example.common.pojo.Task;
import com.example.dao.interfaces.TaskRepository;
import com.example.service.interfaces.TaskStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("taskStorage")
public class TaskStorageImpl implements TaskStorage {

    @Autowired
    @Qualifier("taskRepository")
    private TaskRepository taskRepository;

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getUserTasks(String userId) {
        return taskRepository.getTasksByUserId(userId);
    }

    @Override
    public Task getTaskById(String task) {
        return taskRepository.getTaskById(task);
    }

    @Override
    public Task insert(Task task) {
        return taskRepository.insert(task);
    }

    @Override
    public boolean delete(String task) {
        return taskRepository.delete(task);
    }
}
