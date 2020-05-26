package com.example.dao.interfaces;

import com.example.common.pojo.Conversation;
import com.example.common.pojo.Message;

public interface MessageRepository {
    Message addMessage(Message message);
    Conversation getConversation(String interlocutorA, String interlocutorB);
}
