package com.example.service.interfaces;

import com.example.common.pojo.Notification;

public interface NotificationService {
    void sendNotification(Notification notification);
    void addChannel(long userId, String sessionId, String channelId);
}
