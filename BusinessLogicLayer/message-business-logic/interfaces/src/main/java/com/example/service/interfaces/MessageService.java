package com.example.service.interfaces;

import com.example.common.pojo.AddMessageRequest;
import com.example.common.pojo.Response;

public interface MessageService {
    Response<?> addMessage(AddMessageRequest request, String authenticationToken);
    Response<?> getConversation(String interlocutor, String authenticationToken);
    Response<?> getInterlocutors(String token);
}
