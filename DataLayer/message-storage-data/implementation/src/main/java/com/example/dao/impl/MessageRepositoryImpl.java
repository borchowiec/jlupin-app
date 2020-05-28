package com.example.dao.impl;

import com.example.common.pojo.Message;
import com.example.dao.interfaces.MessageRepository;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Repository(value = "messageRepository")
public class MessageRepositoryImpl implements MessageRepository {
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
    public List<Message> getConversation(String interlocutorA, String  interlocutorB) {
        return messages.values()
                .stream()
                .filter(message -> (message.getSender().equals(interlocutorA) && message.getReceiver().equals(interlocutorB))
                        || (message.getSender().equals(interlocutorB) && message.getReceiver().equals(interlocutorA)))
                .sorted(Comparator.comparing(Message::getSendTime))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getInterlocutors(String userId) {
        List<Pair<LocalDateTime, String>> sendTimeAndInterlocutor = messages.values()
                .stream()
                .filter(message -> message.getSender().equals(userId) || message.getReceiver().equals(userId))
                .map(message -> new Pair<>(message.getSendTime(),
                        message.getSender().equals(userId) ? message.getReceiver() : message.getSender()))
                .sorted(Comparator.comparing(Pair::getKey))
                .collect(Collectors.toList());

        List<String> result = new LinkedList<>();

        for (Pair<LocalDateTime, String> pair : sendTimeAndInterlocutor) {
            if (!result.contains(pair.getValue())) {
                result.add(pair.getValue());
            }
        }

        Collections.reverse(result);
        return result;
    }
}
