package com.example.service.interfaces;

import com.example.common.pojo.AddMessageRequest;
import com.example.common.pojo.Conversation;

public interface MessageService {
    boolean addMessage(AddMessageRequest request, String authenticationToken);
    Conversation getConversation(String interlocutor, String authenticationToken);
}
