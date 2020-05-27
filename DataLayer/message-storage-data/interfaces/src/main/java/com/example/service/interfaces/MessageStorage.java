package com.example.service.interfaces;

import com.example.common.pojo.AddMessageRequest;
import com.example.common.pojo.Conversation;
import com.example.common.pojo.Message;

import java.util.List;

public interface MessageStorage {
    boolean addMessage(AddMessageRequest request);
    List<Message> getConversation(String interlocutorA, String interlocutorB);
    List<String> getInterlocutors(String userId);
}
