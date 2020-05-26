package com.example.dao.impl;

import com.example.common.pojo.Conversation;
import com.example.common.pojo.Message;
import com.example.common.pojo.User;
import com.example.dao.interfaces.MessageRepository;
import com.example.service.interfaces.UserStorage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Repository(value = "messageRepository")
public class MessageRepositoryImpl implements MessageRepository {

    @Autowired
    @Qualifier("userStorage")
    private UserStorage userStorage;
    private Map<String, Message> messages = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(MessageRepositoryImpl.class);

    @Override
    public Message addMessage(Message message) {
        Message copy = new Message(message);
        String id = UUID.randomUUID().toString();
        copy.setId(id);
        messages.put(copy.getId(), copy);
        return copy;
    }

    @Override
    public Conversation getConversation(String interlocutorA, String  interlocutorB) {
        Conversation conversation = new Conversation();

        Map<String, String> interlocutors = userStorage
                .findByIds(interlocutorA, interlocutorB)
                .stream()
                .collect(Collectors.toMap(User::getId, User::getUsername));

        List<Message> conversationMessages = messages.values()
                .stream()
                .filter(message -> (message.getSender().equals(interlocutorA) && message.getReceiver().equals(interlocutorB))
                        || (message.getSender().equals(interlocutorB) && message.getReceiver().equals(interlocutorA)))
                .sorted(Comparator.comparing(Message::getSendTime))
                .collect(Collectors.toList());

        conversation.setMessages(conversationMessages);
        conversation.setInterlocutors(interlocutors);
        return conversation;
    }
}
