package com.example.service.impl;

import com.example.common.pojo.Notification;
import com.example.service.interfaces.NotificationService;
import com.jlupin.impl.client.util.channel.JLupinClientChannelUtil;
import com.jlupin.impl.client.util.channel.exception.JLupinClientChannelUtilException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "notificationService")
public class NotificationServiceImpl implements NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private String channelId;

    @Autowired
    private JLupinClientChannelUtil jLupinClientChannelUtil;

    @Override
    public void sendNotification(Notification notification) {
        try {
            jLupinClientChannelUtil.putNextElementToStreamChannel(channelId, notification);
        } catch (JLupinClientChannelUtilException e) {
            logger.error("Cannot put element to {}.", channelId);
        }
    }

    @Override
    public void setChannel(String channelId) {
        logger.info("setChannel({})", channelId);
        this.channelId = channelId;
    }
}
