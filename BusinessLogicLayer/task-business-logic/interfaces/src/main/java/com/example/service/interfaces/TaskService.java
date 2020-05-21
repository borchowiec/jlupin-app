package com.example.service.interfaces;

import com.example.common.pojo.Task;

import java.util.List;

public interface TaskService {
    Task insert(Task task, String authenticationToken);
    Task save(Task task, String authenticationToken);
    boolean delete(long task, String authenticationToken);
    List<Task> getTasks(String authenticationToken);
}
