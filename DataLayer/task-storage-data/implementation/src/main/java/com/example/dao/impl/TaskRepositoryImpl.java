package com.example.dao.impl;

import com.example.common.pojo.Task;
import com.example.dao.interfaces.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Repository(value = "taskRepository")
public class TaskRepositoryImpl implements TaskRepository {

    private Map<String, Task> tasks = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(TaskRepositoryImpl.class);

    Map<String, Task> getTasks() {
        return Collections.unmodifiableMap(tasks);
    }

    @Override
    public Task save(Task task) {
        Task copy = new Task(task);
        tasks.put(copy.getId(), task);
        logger.info(tasks.toString()); // todo
        return copy;
    }

    @Override
    public List<Task> getTasksByUserId(String userId) {
        logger.info(tasks.toString());
        return tasks.values().stream()
                .filter(task -> task.getOwner().equals(userId))
                .sorted(Comparator.comparing(Task::getCreatedAt))
                .map(Task::new)
                .collect(Collectors.toList());
    }

    @Override
    public Task put(Task task) {
        Task copy = new Task(task);
        String id = UUID.randomUUID().toString();
        copy.setId(id);
        tasks.put(copy.getId(), copy);
        logger.info(tasks.toString());
        return copy;
    }

    @Override
    public boolean delete(String task) {
        return tasks.remove(task) != null;
    }

    @Override
    public Task getTaskById(String task) {
        return tasks.get(task);
    }
}
