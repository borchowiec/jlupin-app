package com.example.common.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AddMessageRequest implements Serializable {
    private String sender;
    private LocalDateTime sendTime = LocalDateTime.now();

    @NotNull
    private String receiver;

    @NotNull
    private String content;
}
