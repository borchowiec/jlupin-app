package com.example.service.impl;

import com.example.common.pojo.AddMessageRequest;
import com.example.common.pojo.Conversation;
import com.example.common.pojo.Notification;
import com.example.common.pojo.NotificationType;
import com.example.service.interfaces.MessageService;
import com.example.service.interfaces.MessageStorage;
import com.example.service.interfaces.NotificationService;
import com.example.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static com.example.common.pojo.NotificationType.MESSAGE;

@Service(value = "messageService")
public class MessageServiceImpl implements MessageService {

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("messageStorage")
    private MessageStorage messageStorage;

    @Autowired
    @Qualifier("notificationService")
    private NotificationService notificationService;

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Override
    public boolean addMessage(AddMessageRequest request, String authenticationToken) {
        String sender = userService.getUserIdFromToken(authenticationToken);
        request.setSender(sender);
        boolean isSaved = messageStorage.addMessage(request);
        if (isSaved) {
            Notification notification = new Notification(request.getReceiver(), sender, MESSAGE, request.getContent());
            notificationService.sendNotification(notification);
        }
        return isSaved;
    }

    @Override
    public Conversation getConversation(String interlocutorA, String authenticationToken) {
        String interlocutorB = userService.getUserIdFromToken(authenticationToken);
        return messageStorage.getConversation(interlocutorA, interlocutorB);
    }
}
