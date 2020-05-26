package com.example.controller;

import com.example.common.pojo.Task;
import com.example.service.interfaces.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TaskController {
    @Autowired
    @Qualifier("taskService")
    private TaskService taskService;

    @PostMapping("/task")
    public Task addTask(@RequestBody @Valid Task task, @RequestHeader("Authorization") String token) {
        return taskService.insert(task, token);
    }

    @GetMapping("/tasks")
    public List<Task> getTasks(@RequestHeader("Authorization") String token) {
        return taskService.getTasks(token);
    }

    @PutMapping("/task")
    public Task updateTask(@RequestBody @Valid Task task, @RequestHeader("Authorization") String token) {
        return taskService.save(task, token);
    }

    @DeleteMapping("/task/{taskId}")
    public boolean removeTask(@PathVariable String taskId, @RequestHeader("Authorization") String token) {
        return taskService.delete(taskId, token);
    }
}
