package com.example.dao.interfaces;

import com.example.common.pojo.Task;

import java.util.List;

public interface TaskRepository {
    Task save(Task task);
    List<Task> getTasksByUserId(String userId);
    Task insert(Task task);
    boolean delete(String task);
    Task getTaskById(String task);
}
