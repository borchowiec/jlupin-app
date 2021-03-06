package com.example.service.impl;

import com.example.common.pojo.Task;
import com.example.common.util.JwtTokenProvider;
import com.example.service.interfaces.TaskService;
import com.example.service.interfaces.TaskStorage;
import com.example.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.common.pojo.TaskStatus.TODO;

@NoArgsConstructor
@AllArgsConstructor
@Service("taskService")
public class TaskServiceImpl implements TaskService {

    @Autowired
    @Qualifier("taskStorage")
    private TaskStorage taskStorage;

    private JwtTokenProvider tokenProvider = JwtTokenProvider.getInstance();

    @Override
    public Task insert(Task task, String authenticationToken) {
        String userId = tokenProvider.getId(authenticationToken);
        task.setOwner(userId);
        task.setStatus(TODO);
        return taskStorage.insert(task);
    }

    @Override
    public Task save(Task task, String authenticationToken) {
        String userId = tokenProvider.getId(authenticationToken);
        task.setOwner(userId);
        Task taskInStorage = taskStorage.getTaskById(task.getId());

        // there is no task of given id, so insert it
        if (taskInStorage == null) {
            return taskStorage.insert(task);
        }

        // update if principal is a owner of given task
        if (userId.equals(taskInStorage.getOwner())) {
            return taskStorage.save(task);
        }

        return null;
    }

    @Override
    public boolean delete(String task, String authenticationToken) {
        String userId = tokenProvider.getId(authenticationToken);
        Task taskInStorage = taskStorage.getTaskById(task);

        // task of given id doesn't exists
        if (taskInStorage == null) {
            return false;
        }

        // if principal is a owner of task
        if (taskInStorage.getOwner().equals(userId)) {
            return taskStorage.delete(task);
        }

        return false;
    }

    @Override
    public List<Task> getTasks(String authenticationToken) {
        String userId = tokenProvider.getId(authenticationToken);
        return taskStorage.getUserTasks(userId);
    }
}
