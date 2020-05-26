package com.example.service.impl;

import com.example.common.pojo.*;
import com.example.common.util.JwtTokenProvider;
import com.example.service.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.common.pojo.NotificationType.MESSAGE;

@Service(value = "messageService")
public class MessageServiceImpl implements MessageService {

    @Autowired
    @Qualifier("userStorage")
    private UserStorage userStorage;

    @Autowired
    @Qualifier("messageStorage")
    private MessageStorage messageStorage;

    private JwtTokenProvider tokenProvider = JwtTokenProvider.getInstance();

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Override
    public boolean addMessage(AddMessageRequest request, String authenticationToken) {
        String sender = tokenProvider.getId(authenticationToken);
        request.setSender(sender);
        return messageStorage.addMessage(request);
    }

    @Override
    public Conversation getConversation(String interlocutorA, String authenticationToken) {
        Conversation conversation = new Conversation();
        String interlocutorB = tokenProvider.getId(authenticationToken);

        // get messages between two users
        List<Message> messages = messageStorage.getConversation(interlocutorA, interlocutorB);
        conversation.setMessages(messages);

        // get usernames
        Map<String, String> interlocutors = userStorage
                .findByIds(interlocutorA, interlocutorB)
                .stream()
                .collect(Collectors.toMap(User::getId, User::getUsername));
        conversation.setInterlocutors(interlocutors);

        return conversation;
    }
}
