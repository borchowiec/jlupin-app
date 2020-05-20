package com.example.controller;

import com.example.common.pojo.AddMessageRequest;
import com.example.service.interfaces.MessageService;
import com.jlupin.impl.serialize.JLupinFSTSerializerImpl;
import com.jlupin.interfaces.serialize.exception.JLupinSerializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MessageController {

    @Autowired
    @Qualifier("messageService")
    private MessageService messageService;

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @PostMapping("/add-message")
    public ResponseEntity<?> addMessage(@RequestBody @Valid AddMessageRequest request) throws JLupinSerializeException {
        HttpStatus httpStatus = messageService.addMessage(request);
        return new ResponseEntity<>(httpStatus);
    }
}