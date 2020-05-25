package com.example.service.impl;

import com.example.common.pojo.Notification;
import com.example.service.interfaces.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service(value = "notificationService")
public class NotificationServiceImpl implements NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Override
    public void sendNotification(Notification notification) {
        logger.info(notification.toString());
    }
}
