package com.example.service.interfaces;

import com.example.common.pojo.AddMessageRequest;
import com.example.common.pojo.Conversation;
import org.springframework.http.HttpStatus;

public interface MessageService {
    HttpStatus addMessage(AddMessageRequest request, String authenticationToken);
    Conversation getConversation(long interlocutor, String authenticationToken);
}
