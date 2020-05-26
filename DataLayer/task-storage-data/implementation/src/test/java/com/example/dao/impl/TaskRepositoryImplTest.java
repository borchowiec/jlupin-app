package com.example.dao.impl;

import com.example.common.pojo.Task;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.common.pojo.TaskStatus.DONE;
import static com.example.common.pojo.TaskStatus.TODO;
import static org.junit.jupiter.api.Assertions.*;

class TaskRepositoryImplTest {

    @Test
    void save_repositoryDoesntContainGivenTask_shouldAddItToRepository() {
        // given
        Map<String, Task> tasks = getExampleTaskMap();
        Task task = new Task("10L", "2L", "Message",
                LocalDateTime.of(2020, 2, 13, 10, 13, 43), TODO);
        assertFalse(tasks.containsKey(task.getId()));

        // when
        TaskRepositoryImpl taskRepository = new TaskRepositoryImpl(tasks);
        Task actual = taskRepository.save(task);

        // then
        Task expected = new Task("10L", "2L", "Message",
                LocalDateTime.of(2020, 2, 13, 10, 13, 43), TODO);

        assertEquals(expected, actual);
        assertNotSame(task, actual);
        assertTrue(taskRepository.getTasks().containsKey(task.getId()));
    }

    @Test
    void save_repositoryContainsGivenTask_shouldUpdateTask() {
        // given
        Map<String, Task> tasks = getExampleTaskMap();
        Task task = new Task("2L", "10L", "mm",
                LocalDateTime.of(2020, 2, 13, 10, 13, 43), DONE);
        assertTrue(tasks.containsKey(task.getId()));

        // when
        TaskRepositoryImpl taskRepository = new TaskRepositoryImpl(tasks);
        Task actual = taskRepository.save(task);

        // then
        Task expected = new Task("2L", "10L", "mm",
                LocalDateTime.of(2020, 2, 13, 10, 13, 43), DONE);

        assertEquals(expected, actual);
        assertEquals(expected, taskRepository.getTasks().get(expected.getId()));
        assertNotSame(task, actual);
    }

    @Test
    void getTasksByUserId_thereIsManyUnorderedTasks_shouldReturnUserTasksOrderedByCreatedAtTime() {
        // given
        String userId = "5L";
        Map<String, Task> tasks = Stream.of(
                new Task("0L", "2L", "Message",
                        LocalDateTime.of(2020, 2, 13, 10, 13, 43), TODO),
                new Task("1L", "5L", "Message",
                        LocalDateTime.of(2020, 2, 16, 13, 13, 43), DONE),
                new Task("2L", "3L", "Message",
                        LocalDateTime.of(2020, 2, 1, 12, 13, 43), TODO),
                new Task("3L", "5L", "Message",
                        LocalDateTime.of(2018, 12, 15, 1, 13, 43), DONE),
                new Task("4L", "5L", "Message",
                        LocalDateTime.of(2020, 1, 17, 12, 13, 43), TODO)
        ).map(task -> new Pair<>(task.getId(), task))
        .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        // when
        TaskRepositoryImpl taskRepository = new TaskRepositoryImpl(tasks);
        List<Task> actual = taskRepository.getTasksByUserId(userId);

        // then
        List<Task> expected = Stream.of(tasks.get("3L"), tasks.get("4L"), tasks.get("1L")).collect(Collectors.toList());
        assertEquals(expected, actual);
    }

    @Test
    void insert_twoTasksInARow_shouldHaveDifferentIds() {
        // given
        LocalDateTime time = LocalDateTime.now();
        Task task1 = new Task(null, "1L", "Some task message", time, TODO);
        Task task2 = new Task(null, "2L", "Some other task message", time, TODO);

        // when
        TaskRepositoryImpl taskRepository = new TaskRepositoryImpl();
        Task actual1 = taskRepository.put(task1);
        Task actual2 = taskRepository.put(task2);

        // then
        assertNotEquals(actual1.getId(), actual2.getId());
        assertNotSame(task1, actual1);
        assertNotSame(task2, actual2);
    }

    @Test
    void delete_taskDoesntExist_shouldReturnFalse() {
        // given
        String taskId = "10L";
        Map<String , Task> tasks = getExampleTaskMap();
        assertFalse(tasks.containsKey(taskId));

        // when
        TaskRepositoryImpl taskRepository = new TaskRepositoryImpl(tasks);
        boolean actual = taskRepository.delete(taskId);

        // then
        assertFalse(actual);
        assertFalse(taskRepository.getTasks().containsKey(taskId));

    }

    @Test
    void delete_taskExists_shouldReturnTrue() {
        // given
        String  taskId = "2L";
        Map<String, Task> tasks = getExampleTaskMap();
        assertTrue(tasks.containsKey(taskId));

        // when
        TaskRepositoryImpl taskRepository = new TaskRepositoryImpl(tasks);
        boolean actual = taskRepository.delete(taskId);

        // then
        assertTrue(actual);
        assertFalse(taskRepository.getTasks().containsKey(taskId));
    }

    private Map<String, Task> getExampleTaskMap() {
        return Stream.of(
                new Task("0L", "2L", "Message",
                        LocalDateTime.of(2020, 2, 13, 10, 13, 43), TODO),
                new Task("1L", "5L", "Todo",
                        LocalDateTime.of(2020, 2, 16, 13, 13, 43), DONE),
                new Task("2L", "3L", "Some other message",
                        LocalDateTime.of(2020, 2, 1, 12, 13, 43), TODO),
                new Task("3L", "5L", "Message",
                        LocalDateTime.of(2018, 12, 15, 1, 13, 43), DONE),
                new Task("4L", "4L", "msg",
                        LocalDateTime.of(2020, 1, 17, 12, 13, 43), TODO)
        ).map(task -> new Pair<>(task.getId(), task))
        .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }
}