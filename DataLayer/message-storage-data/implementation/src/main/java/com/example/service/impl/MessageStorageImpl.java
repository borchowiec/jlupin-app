package com.example.service.impl;

import com.example.common.pojo.AddMessageRequest;
import com.example.common.pojo.Conversation;
import com.example.common.pojo.Message;
import com.example.dao.interfaces.MessageRepository;
import com.example.service.interfaces.MessageStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service(value = "messageStorage")
public class MessageStorageImpl implements MessageStorage {
    @Autowired
    @Qualifier("messageRepository")
    private MessageRepository messageRepository;

    private static final Logger logger = LoggerFactory.getLogger(MessageStorageImpl.class);

    @Override
    public boolean addMessage(AddMessageRequest request) {
        Message message = new Message(request);
        messageRepository.addMessage(message);
        return true;
    }

    @Override
    public Conversation getConversation(String interlocutorA, String interlocutorB) {
        return messageRepository.getConversation(interlocutorA, interlocutorB);
    }
}
