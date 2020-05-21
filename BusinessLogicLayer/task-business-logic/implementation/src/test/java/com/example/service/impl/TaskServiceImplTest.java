package com.example.service.impl;

import com.example.common.pojo.Task;
import com.example.service.interfaces.TaskStorage;
import com.example.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.example.common.pojo.TaskStatus.DONE;
import static com.example.common.pojo.TaskStatus.TODO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {
    private TaskStorage taskStorage;
    private UserService userService;

    @BeforeEach
    void setup() {
        taskStorage = mock(TaskStorage.class);
        userService = mock(UserService.class);
    }

    @Test
    void insert_properData_shouldReturnAddedTask() {
        // given
        Task task = new Task(0L, 0L, "Message", LocalDateTime.now(), null);
        long principalId = 20L;

        // when
        when(taskStorage.insert(any(Task.class))).thenAnswer(args -> {
            ((Task) args.getArgument(0)).setId(30L);
            return args.getArgument(0);
        });
        when(userService.getUserIdFromToken(anyString())).thenReturn(principalId);
        TaskServiceImpl taskService = new TaskServiceImpl(taskStorage, userService);
        Task actual = taskService.insert(task, "Bearer token");

        // then
        Task expected = new Task(30L, principalId, task.getMessage(), task.getCreatedAt(), TODO);
        assertEquals(expected, actual);
    }

    @Test
    void save_taskDoesntExist_shouldReturnInsertedTask() {
        // given
        Task task = new Task(10L, 0L, "Message", LocalDateTime.now(), DONE);
        long principalId = 20L;

        // when
        when(taskStorage.getTaskById(anyLong())).thenReturn(null);
        when(taskStorage.insert(any(Task.class))).thenAnswer(args -> {
            ((Task) args.getArgument(0)).setId(30L);
            return args.getArgument(0);
        });
        when(userService.getUserIdFromToken(anyString())).thenReturn(principalId);
        TaskServiceImpl taskService = new TaskServiceImpl(taskStorage, userService);
        Task actual = taskService.save(task, "Bearer token");

        // then
        Task expected = new Task(30L, principalId, task.getMessage(), task.getCreatedAt(), task.getStatus());
        assertEquals(expected, actual);
    }

    @Test
    void save_taskExistsAndPrincipalIsAOwner_shouldReturnUpdatedTask() {
        // given
        long principalId = 20L;
        Task task = new Task(10L, 0L, "Message", LocalDateTime.now(), DONE);
        Task taskInStorage = new Task(10L, principalId, "no message", LocalDateTime.now().minusDays(12), TODO);

        // when
        when(taskStorage.getTaskById(anyLong())).thenReturn(taskInStorage);
        when(taskStorage.save(any(Task.class))).thenAnswer(args -> args.getArgument(0));
        when(userService.getUserIdFromToken(anyString())).thenReturn(principalId);
        TaskServiceImpl taskService = new TaskServiceImpl(taskStorage, userService);
        Task actual = taskService.save(task, "Bearer token");

        // then
        Task expected = new Task(task);
        expected.setOwner(principalId);
        assertEquals(expected, actual);
    }

    @Test
    void save_taskExistsButPrincipalIsNotAOwner_shouldReturnNull() {
        // given
        long principalId = 20L;
        Task task = new Task(10L, 0L, "Message", LocalDateTime.now(), DONE);
        Task taskInStorage = new Task(10L, principalId + 1L, "no message", LocalDateTime.now().minusDays(12), TODO);

        // when
        when(taskStorage.getTaskById(anyLong())).thenReturn(taskInStorage);
        when(userService.getUserIdFromToken(anyString())).thenReturn(principalId);
        TaskServiceImpl taskService = new TaskServiceImpl(taskStorage, userService);
        Task actual = taskService.save(task, "Bearer token");

        // then
        assertNull(actual);
    }

    @Test
    void delete_taskDoesntExist_shouldReturnFalse() {
        // given
        long principalId = 20L;
        long taskId = 100L;

        // when
        when(userService.getUserIdFromToken(anyString())).thenReturn(principalId);
        when(taskStorage.getTaskById(taskId)).thenReturn(null);
        TaskServiceImpl taskService = new TaskServiceImpl(taskStorage, userService);
        boolean actual = taskService.delete(taskId, "Bearer token");

        // then
        assertFalse(actual);
    }

    @Test
    void delete_taskExistsAndPrincipalIsAOwnerOfTask_shouldReturnTrue() {
        // given
        long principalId = 20L;
        long taskId = 100L;
        Task taskInStorage = new Task(taskId, principalId, "Some message", LocalDateTime.now(), DONE);

        // when
        when(userService.getUserIdFromToken(anyString())).thenReturn(principalId);
        when(taskStorage.getTaskById(anyLong())).thenReturn(taskInStorage);
        when(taskStorage.delete(anyLong())).thenReturn(true);
        TaskServiceImpl taskService = new TaskServiceImpl(taskStorage, userService);
        boolean actual = taskService.delete(taskId, "Bearer token");

        // then
        assertTrue(actual);
    }

    @Test
    void delete_taskExistsButPrincipalIsNotAOwnerOfTask_shouldReturnFalse() {
        // given
        long principalId = 20L;
        long taskId = 100L;
        Task taskInStorage = new Task(taskId, principalId + 1, "Some message", LocalDateTime.now(), DONE);

        // when
        when(userService.getUserIdFromToken(anyString())).thenReturn(principalId);
        when(taskStorage.getTaskById(anyLong())).thenReturn(taskInStorage);
        TaskServiceImpl taskService = new TaskServiceImpl(taskStorage, userService);
        boolean actual = taskService.delete(taskId, "Bearer token");

        // then
        assertFalse(actual);
    }

    @Test
    void getTasks_properData_shouldReturnUserTasks() {
        // given
        long principalId = 20L;
        List<Task> tasks = Collections.singletonList(
                new Task(10L, principalId, "Some message", LocalDateTime.now(), DONE));

        // when
        when(userService.getUserIdFromToken(anyString())).thenReturn(principalId);
        when(taskStorage.getUserTasks(anyLong())).thenReturn(tasks);
        TaskServiceImpl taskService = new TaskServiceImpl(taskStorage, userService);
        List<Task> actual = taskService.getTasks("Bearer token");

        // then
        assertEquals(tasks, actual);
    }
}