package com.example.common.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class Conversation implements Serializable {
    private Map<Long, String> interlocutors; // {userId : username}
    private List<Message> messages;
}
