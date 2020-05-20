package com.example.common.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AddMessageRequest implements Serializable {
    private long sender;
    private LocalDateTime sendTime = LocalDateTime.now();

    @NotNull
    private long receiver;

    @NotNull
    private String content;
}
