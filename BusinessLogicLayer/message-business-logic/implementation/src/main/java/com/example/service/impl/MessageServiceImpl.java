package com.example.service.impl;

import com.example.common.pojo.AddMessageRequest;
import com.example.common.pojo.Conversation;
import com.example.service.interfaces.MessageService;
import com.example.service.interfaces.MessageStorage;
import com.example.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service(value = "messageService")
public class MessageServiceImpl implements MessageService {

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("messageStorage")
    private MessageStorage messageStorage;

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Override
    public boolean addMessage(AddMessageRequest request, String authenticationToken) {
        long sender = userService.getUserIdFromToken(authenticationToken);
        request.setSender(sender);
        return messageStorage.addMessage(request);
    }

    @Override
    public Conversation getConversation(long interlocutorA, String authenticationToken) {
        long interlocutorB = userService.getUserIdFromToken(authenticationToken);
        return messageStorage.getConversation(interlocutorA, interlocutorB);
    }
}
