package com.example.service.interfaces;

import com.example.common.pojo.AddMessageRequest;
import com.example.common.pojo.Response;
import com.example.common.pojo.UserInfo;

import java.util.List;

public interface MessageService {
    Response<?> addMessage(AddMessageRequest request, String authenticationToken);
    Response<?> getConversation(String interlocutor, String authenticationToken);
    List<UserInfo> getInterlocutors(String token);
}
