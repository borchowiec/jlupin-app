package com.example.service.interfaces;

import com.example.common.pojo.AddMessageRequest;
import com.example.common.pojo.Conversation;
import org.springframework.http.HttpStatus;

public interface MessageStorage {
    HttpStatus addMessage(AddMessageRequest request);
    Conversation getConversation(long interlocutorA, long interlocutorB);
}
