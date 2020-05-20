package com.example.service.impl;

import com.example.common.pojo.AddMessageRequest;
import com.example.common.pojo.Message;
import com.example.dao.interfaces.MessageRepository;
import com.example.service.interfaces.MessageStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service(value = "messageStorage")
public class MessageStorageImpl implements MessageStorage {
    @Autowired
    @Qualifier("messageRepository")
    private MessageRepository messageRepository;

    @Override
    public HttpStatus addMessage(AddMessageRequest request) {
        Message message = new Message(request);
        messageRepository.addMessage(message);
        return HttpStatus.OK;
    }
}
