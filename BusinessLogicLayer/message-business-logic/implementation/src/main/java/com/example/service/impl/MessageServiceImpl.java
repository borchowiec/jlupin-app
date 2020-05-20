package com.example.service.impl;

import com.example.common.pojo.AddMessageRequest;
import com.example.service.interfaces.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service(value = "messageService")
public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Override
    public HttpStatus addMessage(AddMessageRequest request) {
        return HttpStatus.OK;
    }
}
