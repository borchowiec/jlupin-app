package com.example.dao.impl;

import com.example.common.pojo.Message;
import com.example.dao.interfaces.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository(value = "messageRepository")
public class MessageRepositoryImpl implements MessageRepository {

    private Map<Long, Message> messages = new HashMap<>();
    private static long nextId = 0L;
    private static final Logger logger = LoggerFactory.getLogger(MessageRepositoryImpl.class);

    @Override
    public Message addMessage(Message message) {
        Message copy = new Message(message);
        copy.setId(nextId);
        nextId++;
        messages.put(copy.getId(), copy);
        logger.info(messages.toString());
        return copy;
    }
}
