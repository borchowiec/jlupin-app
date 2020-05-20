package com.example.dao.impl;

import com.example.common.pojo.Conversation;
import com.example.common.pojo.Message;
import com.example.common.pojo.User;
import com.example.service.interfaces.UserStorage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class MessageRepositoryImplTest {

    private UserStorage userStorage;

    @Test
    void getConversation_messagesAreStoredInWrongOrder_shouldReturnListOfMessagesSortedBySendTime() {
        // given
        User interlocutorA = new User(1L, "usernameA", "pass");
        User interlocutorB = new User(2L, "usernameB", "pass");

        Map<Long, Message> messages = new HashMap<>();
        messages.put(0L, new Message(0L, 1L, 2L, LocalDateTime.of(2020, 3,
                12, 7, 12, 34), "Some content"));
        messages.put(1L, new Message(1L, 2L, 1L, LocalDateTime.of(2019, 3,
                12, 7, 12, 34), "Message"));
        messages.put(2L, new Message(2L, 2L, 2L, LocalDateTime.of(2020, 5,
                12, 7, 12, 34), "Some content"));
        messages.put(3L, new Message(3L, 3L, 2L, LocalDateTime.of(2020, 1,
                12, 7, 12, 34), "Some content"));
        messages.put(4L, new Message(4L, 1L, 2L, LocalDateTime.of(2020, 9,
                12, 7, 12, 34), "Content"));

        // when
        userStorage = Mockito.mock(UserStorage.class);
        when(userStorage.findByIds(any()))
                .thenReturn(Stream.of(interlocutorA, interlocutorB).collect(Collectors.toList()));
        MessageRepositoryImpl messageRepository = new MessageRepositoryImpl(userStorage, messages);
        Conversation actual = messageRepository.getConversation(interlocutorA.getId(), interlocutorB.getId());

        // then
        Conversation expected = new Conversation();
        Map<Long, String> interlocutors = new HashMap<>();
        interlocutors.put(interlocutorA.getId(), interlocutorA.getUsername());
        interlocutors.put(interlocutorB.getId(), interlocutorB.getUsername());
        expected.setInterlocutors(interlocutors);
        expected.setMessages(Stream.of(messages.get(1L), messages.get(0L), messages.get(4L))
                .collect(Collectors.toList()));

        assertEquals(expected, actual);
    }
}