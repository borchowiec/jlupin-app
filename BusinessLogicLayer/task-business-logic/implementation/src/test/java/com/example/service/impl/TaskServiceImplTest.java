package com.example.service.impl;

import com.example.common.pojo.Task;
import com.example.common.util.JwtTokenProvider;
import com.example.service.interfaces.TaskStorage;
import com.example.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.example.common.pojo.TaskStatus.DONE;
import static com.example.common.pojo.TaskStatus.TODO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {
    private TaskStorage taskStorage;
    private JwtTokenProvider tokenProvider;

    @BeforeEach
    void setup() {
        taskStorage = mock(TaskStorage.class);
        tokenProvider = mock(JwtTokenProvider.class);
    }

    @Test
    void insert_properData_shouldReturnAddedTask() {
        // given
        Task task = new Task(null, null, "Message", LocalDateTime.now(), null);
        String principalId = UUID.randomUUID().toString();

        // when
        String taskId = UUID.randomUUID().toString();
        when(taskStorage.insert(any(Task.class))).thenAnswer(args -> {
            ((Task) args.getArgument(0)).setId(taskId);
            return args.getArgument(0);
        });
        when(tokenProvider.getId(anyString())).thenReturn(principalId);
        TaskServiceImpl taskService = new TaskServiceImpl(taskStorage, tokenProvider);
        Task actual = taskService.insert(task, "Bearer token");

        // then
        Task expected = new Task(taskId, principalId, task.getMessage(), task.getCreatedAt(), TODO);
        assertEquals(expected, actual);
    }

    @Test
    void save_taskDoesntExist_shouldReturnInsertedTask() {
        // given
        String taskId = UUID.randomUUID().toString();
        String principalId = UUID.randomUUID().toString();
        Task task = new Task(taskId, null, "Message", LocalDateTime.now(), DONE);

        // when
        String finalTaskId = UUID.randomUUID().toString();
        when(taskStorage.getTaskById(anyString())).thenReturn(null);
        when(taskStorage.insert(any(Task.class))).thenAnswer(args -> {
            ((Task) args.getArgument(0)).setId(finalTaskId);
            return args.getArgument(0);
        });
        when(tokenProvider.getId(anyString())).thenReturn(principalId);
        TaskServiceImpl taskService = new TaskServiceImpl(taskStorage, tokenProvider);
        Task actual = taskService.save(task, "Bearer token");

        // then
        Task expected = new Task(finalTaskId, principalId, task.getMessage(), task.getCreatedAt(), task.getStatus());
        assertEquals(expected, actual);
    }

    @Test
    void save_taskExistsAndPrincipalIsAOwner_shouldReturnUpdatedTask() {
        // given
        String taskId = UUID.randomUUID().toString();
        String principalId = UUID.randomUUID().toString();
        Task task = new Task(taskId, null, "Message", LocalDateTime.now(), DONE);
        Task taskInStorage = new Task(taskId, principalId, "no message", LocalDateTime.now().minusDays(12), TODO);

        // when
        when(taskStorage.getTaskById(anyString())).thenReturn(taskInStorage);
        when(taskStorage.save(any(Task.class))).thenAnswer(args -> args.getArgument(0));
        when(tokenProvider.getId(anyString())).thenReturn(principalId);
        TaskServiceImpl taskService = new TaskServiceImpl(taskStorage, tokenProvider);
        Task actual = taskService.save(task, "Bearer token");

        // then
        Task expected = new Task(task);
        expected.setOwner(principalId);
        assertEquals(expected, actual);
    }

    @Test
    void save_taskExistsButPrincipalIsNotAOwner_shouldReturnNull() {
        // given
        String taskId = UUID.randomUUID().toString();
        String principalId = UUID.randomUUID().toString();
        Task task = new Task(taskId, null, "Message", LocalDateTime.now(), DONE);
        Task taskInStorage = new Task(taskId, principalId + "char", "no message", LocalDateTime.now().minusDays(12), TODO);

        // when
        when(taskStorage.getTaskById(anyString())).thenReturn(taskInStorage);
        when(tokenProvider.getId(anyString())).thenReturn(principalId);
        TaskServiceImpl taskService = new TaskServiceImpl(taskStorage, tokenProvider);
        Task actual = taskService.save(task, "Bearer token");

        // then
        assertNull(actual);
    }

    @Test
    void delete_taskDoesntExist_shouldReturnFalse() {
        // given
        String taskId = UUID.randomUUID().toString();
        String principalId = UUID.randomUUID().toString();

        // when
        when(tokenProvider.getId(anyString())).thenReturn(principalId);
        when(taskStorage.getTaskById(taskId)).thenReturn(null);
        TaskServiceImpl taskService = new TaskServiceImpl(taskStorage, tokenProvider);
        boolean actual = taskService.delete(taskId, "Bearer token");

        // then
        assertFalse(actual);
    }

    @Test
    void delete_taskExistsAndPrincipalIsAOwnerOfTask_shouldReturnTrue() {
        // given
        String taskId = UUID.randomUUID().toString();
        String principalId = UUID.randomUUID().toString();
        Task taskInStorage = new Task(taskId, principalId, "Some message", LocalDateTime.now(), DONE);

        // when
        when(tokenProvider.getId(anyString())).thenReturn(principalId);
        when(taskStorage.getTaskById(anyString())).thenReturn(taskInStorage);
        when(taskStorage.delete(anyString())).thenReturn(true);
        TaskServiceImpl taskService = new TaskServiceImpl(taskStorage, tokenProvider);
        boolean actual = taskService.delete(taskId, "Bearer token");

        // then
        assertTrue(actual);
    }

    @Test
    void delete_taskExistsButPrincipalIsNotAOwnerOfTask_shouldReturnFalse() {
        // given
        String taskId = UUID.randomUUID().toString();
        String principalId = UUID.randomUUID().toString();
        Task taskInStorage = new Task(taskId, principalId + 1, "Some message", LocalDateTime.now(), DONE);

        // when
        when(tokenProvider.getId(anyString())).thenReturn(principalId);
        when(taskStorage.getTaskById(anyString())).thenReturn(taskInStorage);
        TaskServiceImpl taskService = new TaskServiceImpl(taskStorage, tokenProvider);
        boolean actual = taskService.delete(taskId, "Bearer token");

        // then
        assertFalse(actual);
    }

    @Test
    void getTasks_properData_shouldReturnUserTasks() {
        // given
        String taskId = UUID.randomUUID().toString();
        String principalId = UUID.randomUUID().toString();
        List<Task> tasks = Collections.singletonList(
                new Task(taskId, principalId, "Some message", LocalDateTime.now(), DONE));

        // when
        when(tokenProvider.getId(anyString())).thenReturn(principalId);
        when(taskStorage.getUserTasks(anyString())).thenReturn(tasks);
        TaskServiceImpl taskService = new TaskServiceImpl(taskStorage, tokenProvider);
        List<Task> actual = taskService.getTasks("Bearer token");

        // then
        assertEquals(tasks, actual);
    }
}