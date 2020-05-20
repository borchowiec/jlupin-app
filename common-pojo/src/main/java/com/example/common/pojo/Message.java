package com.example.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    private long id;
    private long sender;
    private long receiver;
    private LocalDateTime sendTime;
    private String content;

    public Message(Message message) {
        this.id = message.getId();
        this.sender = message.getSender();
        this.receiver = message.getReceiver();
        this.sendTime = message.getSendTime();
        this.content = message.getContent();
    }

    public Message(AddMessageRequest request) {
        this.content = request.getContent();
        this.sender = request.getSender();
        this.receiver = request.getReceiver();
        this.sendTime = request.getSendTime();
    }
}
