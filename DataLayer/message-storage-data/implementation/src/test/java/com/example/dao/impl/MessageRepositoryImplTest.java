package com.example.dao.impl;

import com.example.common.pojo.Message;
import com.example.dao.interfaces.MessageRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MessageRepositoryImplTest {

    @Test
    void getInterlocutors_messagesAreInWrongOrder_getInterlocutorsByLatestMessage() {
        // given
        String userId = "12";
        Map<String, Message> messages = new HashMap<>();
        messages.put("a", new Message("a", userId, "13",
                LocalDateTime.of(2016, 1, 1, 12, 23,1), "content"));
        messages.put("b", new Message("b", "asd23", userId,
                LocalDateTime.of(2020, 1, 1, 12, 23,1), "content"));
        messages.put("c", new Message("c", "asd", userId,
                LocalDateTime.of(2018, 1, 1, 12, 23,1), "content"));
        messages.put("d", new Message("d", "asd", "13",
                LocalDateTime.of(2019, 1, 1, 12, 23,1), "content"));
        messages.put("e", new Message("e", userId, "13",
                LocalDateTime.of(2017, 1, 1, 12, 23,1), "content"));

        // when
        MessageRepository messageRepository = new MessageRepositoryImpl(messages);
        List<String> actual = messageRepository.getInterlocutors(userId);

        // then
        List<String> expected = Stream.of("asd23", "asd", "13").collect(Collectors.toList());
        assertEquals(expected, actual);
    }
}