package com.example.common.pojo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Message implements Serializable {
    private long id;
    private long sender;
    private long receiver;
    private LocalDateTime sendTime;
    private String content;
}
