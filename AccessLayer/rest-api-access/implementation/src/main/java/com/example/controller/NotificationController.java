package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @MessageMapping("/notification-ws/{userId}/subscribe")
    public void subscribe(@DestinationVariable long userId, SimpMessageHeaderAccessor headerAccessor) {
        logger.info("subscribe");
    }
}
