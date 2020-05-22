package com.example.controller;

import com.example.common.pojo.AddMessageRequest;
import com.example.common.pojo.Conversation;
import com.example.service.interfaces.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class MessageController {

    @Autowired
    @Qualifier("messageService")
    private MessageService messageService;

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @PostMapping("/add-message")
    public boolean addMessage(@RequestBody @Valid AddMessageRequest request,
                                        @RequestHeader("Authorization") String token) {
        return messageService.addMessage(request, token);
    }

    @GetMapping("/conversation/{interlocutorId}")
    public Conversation getConversation(@PathVariable long interlocutorId,
                                        @RequestHeader("Authorization") String token) {
        return messageService.getConversation(interlocutorId, token);
    }
}
