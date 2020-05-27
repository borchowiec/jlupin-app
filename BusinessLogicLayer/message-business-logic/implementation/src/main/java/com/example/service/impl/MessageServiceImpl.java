package com.example.service.impl;

import com.example.common.pojo.*;
import com.example.common.util.JwtTokenProvider;
import com.example.service.interfaces.MessageService;
import com.example.service.interfaces.MessageStorage;
import com.example.service.interfaces.UserStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public Response<?> addMessage(AddMessageRequest request, String authenticationToken) {
        String sender = tokenProvider.getId(authenticationToken);

        if (!userStorage.existsById(sender)) {
            String msg = String.format("Not found user of id %s. Try to log out and log in.", request.getReceiver());
            return new Response<>(new ErrorMessage(msg), HttpStatus.BAD_REQUEST);
        }
        request.setSender(sender);

        if (!userStorage.existsById(request.getReceiver())) {
            String msg = String.format("Not found user of id %s.", request.getReceiver());
            return new Response<>(new ErrorMessage(msg), HttpStatus.BAD_REQUEST);
        }

        return new Response<>(messageStorage.addMessage(request), HttpStatus.OK);
    }

    @Override
    public Response<?> getConversation(String interlocutorA, String authenticationToken) {
        Conversation conversation = new Conversation();
        String interlocutorB = tokenProvider.getId(authenticationToken);

        if (!userStorage.existsById(interlocutorB)) {
            String msg = String.format("Not found user of id %s. Try to log out and log in.", interlocutorB);
            return new Response<>(new ErrorMessage(msg), HttpStatus.BAD_REQUEST);
        }

        if (!userStorage.existsById(interlocutorA)) {
            String msg = String.format("Not found user of id %s.", interlocutorA);
            return new Response<>(new ErrorMessage(msg), HttpStatus.BAD_REQUEST);
        }

        // get messages between two users
        List<Message> messages = messageStorage.getConversation(interlocutorA, interlocutorB);
        conversation.setMessages(messages);

        // get usernames
        Map<String, String> interlocutors = userStorage
                .findByIds(interlocutorA, interlocutorB)
                .stream()
                .collect(Collectors.toMap(User::getId, User::getUsername));
        conversation.setInterlocutors(interlocutors);

        return new Response<>(conversation, HttpStatus.OK);
    }

    @Override
    public Response<?> getInterlocutors(String token) {
        String userId = tokenProvider.getId(token);
        List<String> interlocutorsIds = messageStorage.getInterlocutors(userId);

        if (!userStorage.existsById(userId)) {
            String msg = String.format("Not found user of id %s. Try to log out and log in.", userId);
            return new Response<>(new ErrorMessage(msg), HttpStatus.BAD_REQUEST);
        }

        List<UserInfo> collect = userStorage
                .findByIds(interlocutorsIds.toArray(new String[interlocutorsIds.size()]))
                .stream()
                .map(UserInfo::new)
                .collect(Collectors.toList());
        return new Response<>(collect, HttpStatus.OK);
    }
}
