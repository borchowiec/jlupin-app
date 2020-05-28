package com.example.service.interfaces;

import com.example.common.pojo.Notification;

public interface NotificationService {
    void sendNotification(Notification notification);
    void addChannel(String userId, String sessionId, String channelId);
    void closeChannel(String sessionId);
    void setChannel(String channelId);
}
