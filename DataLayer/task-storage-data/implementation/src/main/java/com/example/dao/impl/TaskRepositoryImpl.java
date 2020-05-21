package com.example.dao.impl;

import com.example.common.pojo.Task;
import com.example.dao.interfaces.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Repository(value = "taskRepository")
public class TaskRepositoryImpl implements TaskRepository {

    private Map<Long, Task> tasks = new HashMap<>();
    private static long nextId = 0;

    Map<Long, Task> getTasks() {
        return Collections.unmodifiableMap(tasks);
    }

    @Override
    public Task save(Task task) {
        Task copy = new Task(task);
        tasks.put(copy.getId(), task);
        return copy;
    }

    @Override
    public List<Task> getTasksByUserId(long userId) {
        return tasks.values().stream()
                .filter(task -> task.getOwner() == userId)
                .sorted(Comparator.comparing(Task::getCreatedAt))
                .map(Task::new)
                .collect(Collectors.toList());
    }

    @Override
    public Task put(Task task) {
        Task copy = new Task(task);
        copy.setId(nextId);
        nextId++;
        tasks.put(copy.getId(), copy);
        return copy;
    }

    @Override
    public boolean delete(long task) {
        return tasks.remove(task) != null;
    }

    @Override
    public Task getTaskById(long task) {
        return tasks.get(task);
    }
}
