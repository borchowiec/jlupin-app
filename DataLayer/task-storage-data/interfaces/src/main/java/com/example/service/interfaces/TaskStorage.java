package com.example.service.interfaces;

import com.example.common.pojo.Task;

import java.util.List;

public interface TaskStorage {
    Task save(Task task);
    List<Task> getUserTasks(String userId);
    Task getTaskById(String task);
    Task insert(Task task);
    boolean delete(String task);
}
