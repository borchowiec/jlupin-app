package com.example.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Serializable {
    private long id;
    private long owner;

    @NotNull
    private String message;

    private LocalDateTime createdAt = LocalDateTime.now();

    private TaskStatus status;

    public Task(Task task) {
        this.id = task.getId();
        this.owner = task.getOwner();
        this.message = task.getMessage();
        this.status = task.getStatus();
        this.createdAt = task.getCreatedAt();
    }
}
