package com.example.dao.interfaces;

import com.example.common.pojo.Message;

import java.util.List;

public interface MessageRepository {
    Message addMessage(Message message);
    List<Message> getConversation(String interlocutorA, String interlocutorB);
    List<String> getInterlocutors(String userId);
}
