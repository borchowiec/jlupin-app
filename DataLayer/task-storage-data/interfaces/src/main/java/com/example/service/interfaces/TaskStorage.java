package com.example.service.interfaces;

import com.example.common.pojo.Task;

import java.util.List;

public interface TaskStorage {
    Task save(Task task);
    List<Task> getUserTasks(long userId);
    Task getTaskById(long task);
    Task put(Task task);
    boolean delete(long task);
}
