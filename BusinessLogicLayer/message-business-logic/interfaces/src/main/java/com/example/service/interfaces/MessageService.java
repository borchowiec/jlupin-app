package com.example.service.interfaces;

import com.example.common.pojo.AddMessageRequest;
import org.springframework.http.HttpStatus;

public interface MessageService {
    HttpStatus addMessage(AddMessageRequest request);
}
