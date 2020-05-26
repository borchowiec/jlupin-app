package com.example.service.interfaces;

import com.example.common.pojo.AddMessageRequest;
import com.example.common.pojo.Conversation;

public interface MessageStorage {
    boolean addMessage(AddMessageRequest request);
    Conversation getConversation(String interlocutorA, String interlocutorB);
}
