package com.example.dao.interfaces;

import com.example.common.pojo.Task;

import java.util.List;

public interface TaskRepository {
    Task save(Task task);
    List<Task> getTasksByUserId(long userId);
    Task put(Task task);
    boolean delete(long task);
    Task getTaskById(long task);
}
